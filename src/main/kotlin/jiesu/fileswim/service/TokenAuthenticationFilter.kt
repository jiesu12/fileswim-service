package jiesu.fileswim.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import jiesu.fileswim.service.model.AuthException
import jiesu.fileswim.service.model.PublicKeyHolder
import jiesu.fileswim.service.model.TokenPurpose
import jiesu.fileswim.service.model.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

/**
 * Filter to parse token from http requests.
*/
class TokenAuthenticationFilter(val objectMapper: ObjectMapper, val publicKeyHolder: PublicKeyHolder) : GenericFilterBean() {

    companion object {
        val log: Logger = LoggerFactory.getLogger(TokenAuthenticationFilter::class.java)
        const val TOKEN_NAME = "fstoken"
    }

    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
        val request = req as HttpServletRequest
        val user: User? = getTokenFromHeader(request)
        if (user != null) {
            SecurityContextHolder.getContext().authentication =
                    PreAuthenticatedAuthenticationToken(user, null, emptyList())
        } else {
            val userWithLink: User? = getTokenFromRequestParams(request)
            if (userWithLink != null) {
                SecurityContextHolder.getContext().authentication =
                        PreAuthenticatedAuthenticationToken(userWithLink, null, emptyList())
            }
        }
        chain.doFilter(req, res)
    }

    private fun getTokenFromHeader(request: HttpServletRequest): User? {
        val token = request.getHeader(TOKEN_NAME)
        if (token != null) {
            try {
                return parseToken(token, TokenPurpose.LOGIN)
            } catch (e: Exception) {
                log.debug("Invalid token in header, reason - {}", e.toString())
            }
        }
        return null
    }

    /**
     * For download link or html5 audio/video tag, we can't use custom http header to provide token.
     * So we use a short life token for such purpose. This token can be provided as URL GET
     * parameter.
     */
    private fun getTokenFromRequestParams(request: HttpServletRequest): User? {
        val token = request.getParameter(TOKEN_NAME)
        if (token != null) {
            try {
                return parseToken(token, TokenPurpose.HTML_LINK)
            } catch (e: Exception) {
                log.debug("Invalid token in URL param, reason - {}", e.toString())
            }
        }
        return null
    }

    fun parseToken(token: String, tokenPurpose: TokenPurpose): User {
        return try {
            val body: Claims = Jwts.parserBuilder()
                    .setSigningKey(publicKeyHolder.publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .body
            if (tokenPurpose.name != body.audience) {
                throw AuthException("Wrong type of token provided.")
            }
            objectMapper.readValue(body.subject, User::class.java)
        } catch (e: ExpiredJwtException) {
            throw AuthException("Token has expired", e)
        } catch (e: IOException) {
            throw AuthException("Invalid token", e)
        } catch (e: AuthException) {
            throw e
        } catch (e: RuntimeException) {
            throw AuthException("Invalid token", e)
        }
    }
}

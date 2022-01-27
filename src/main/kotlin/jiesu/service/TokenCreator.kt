package jiesu.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import jiesu.service.model.TokenPurpose
import java.security.KeyPair
import java.util.*

/**
 * JWT signature algo is decided by secret key's type, algo, and size.
 * See {@link io.jsonwebtoken.SignatureAlgorithm#forSigningKey(Key)}.
 *
 * Key Type    Key Algo     Key Size                Signature Algo
 * -------------------------------------------------------------------
 * SecretKey   HmacSHA256   256 <= size <= 383      HS256
 * SecretKey   HmacSHA384   384 <= size <= 511      HS384
 * SecretKey   HmacSHA512   512 <= size             HS512
 * ECKey       PrivateKey   256 <= size <= 383      ES256
 * ECKey       PrivateKey   384 <= size <= 511      ES384
 * ECKey       PrivateKey   4096 <= size            ES512
 * RSAKey      PrivateKey   2048 <= size <= 3071    RS256
 * RSAKey      PrivateKey   3072 <= size <= 4095    RS384
 * RSAKey      PrivateKey   4096 <= size            RS512
 *
 * The private key's algo and size can be retrived by:
 * openssl pkey -in token_private_key.pem -noout -text
 * 
 * For javaswim token's private key, the output of above command is:
 * RSA Private-Key: (3072 bit, 2 primes) 
 * 
 * By looking up the table above, the JWT's signature algo should be RS384.
 * 
 * You can also find out JWT signature's algo by inspecting the JWT token itself.
 * Paste the token to <https://jwt.io/> and check the header.
 */
class TokenCreator(private val objectMapper: ObjectMapper, private val keyPair: KeyPair) {

    fun createToken(dto: Any, tokenPurpose: TokenPurpose, tokenTtlInSecs: Long): String {
        val issuedAt = Date()
        val exp = Date(issuedAt.time + tokenTtlInSecs * 1000)
        return Jwts.builder()
            .setSubject(toJson(dto))
            .setExpiration(exp)
            .setAudience(tokenPurpose.name)
            .setIssuedAt(issuedAt)
            .signWith(keyPair.private)
            .compact()
    }

    private fun toJson(dto: Any): String {
        return try {
            objectMapper.writeValueAsString(dto)
        } catch (e: Exception) {
            throw RuntimeException("Failed to convert to json for $dto", e)
        }
    }
}

package jiesu.fileswim.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import jiesu.fileswim.service.model.TokenPurpose
import java.security.KeyPair
import java.util.*

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
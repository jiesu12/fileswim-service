package jiesu.service.config

import com.fasterxml.jackson.databind.ObjectMapper
import jiesu.service.token.PublicKeyUpdater
import jiesu.service.token.TokenAuthenticationFilter
import jiesu.service.model.PublicKeyHolder
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(name = ["jiesu.service.token"], havingValue = "true", matchIfMissing = true)
class TokenConfig(val discoveryClient: DiscoveryClient) {
    @Bean
    fun publicKeyHolder(): PublicKeyHolder =
        PublicKeyHolder(null)

    @Bean
    fun tokenAuthenticationFilter(objectMapper: ObjectMapper): TokenAuthenticationFilter =
        TokenAuthenticationFilter(objectMapper, publicKeyHolder())

    @Bean
    fun publicKeyUpdater(): PublicKeyUpdater =
        PublicKeyUpdater(discoveryClient,  publicKeyHolder())
}
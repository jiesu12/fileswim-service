package jiesu.fileswim.service

import jiesu.fileswim.service.model.PublicKeyHolder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
class PublicKeyConfig {
    @Bean
    fun publicKeyHolder(): PublicKeyHolder = PublicKeyHolder(null)
}
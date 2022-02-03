package jiesu.service.config

import jiesu.service.token.PublicKeyUpdater
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@ConditionalOnProperty(name = ["jiesu.service.token.schedule"], havingValue = "true", matchIfMissing = true)
class TokenScheduleConfig(private val publicKeyUpdater: PublicKeyUpdater) {
    @Scheduled(fixedRate = 30000)
    fun refreshPublicKey() =
        publicKeyUpdater.refreshPublicKey()
}
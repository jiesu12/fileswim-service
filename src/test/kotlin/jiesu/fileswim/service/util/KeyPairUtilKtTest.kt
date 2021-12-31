package jiesu.fileswim.service.util

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class KeyPairUtilKtTest {

    @Test
    fun createKeyPairTest() {
        val keyPair = createKeyPair(
            this.javaClass::class.java.getResource("/token_private_key.der").file,
            this.javaClass::class.java.getResource("/token_public_key.der").file)
        assertNotNull(keyPair)
    }
}
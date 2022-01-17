package jiesu.fileswim.service.util

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class KeyPairUtilKtTest {

    @Test
    fun loadKeyPairFromDerFileTest() {
        val keyPair = loadKeyPairFromDerFile(
            this.javaClass::class.java.getResource("/token_private_key.der").file,
            this.javaClass::class.java.getResource("/token_public_key.der").file)
        assertNotNull(keyPair)
    }

    @Test
    fun loadKeyPairFromPemFileTest() {
        val keyPair = loadKeyPairFromPemFile(
            this.javaClass::class.java.getResource("/token_private_key.pem").file,
            this.javaClass::class.java.getResource("/token_public_key.pem").file)
        assertNotNull(keyPair)
    }
}

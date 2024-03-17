package jiesu.service.util

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class KeyPairUtilKtTest {

    @Test
    fun loadKeyPairFromDerFileTest() {
        val keyPair = loadKeyPairFromDerFile(
            File("src/test/resources/token_private_key.der").path,
            File("src/test/resources/token_public_key.der").path)
        assertNotNull(keyPair)
    }

    @Test
    fun loadKeyPairFromPemFileTest() {
        val keyPair = loadKeyPairFromPemFile(
            File("src/test/resources/token_private_key.pem").path,
            File("src/test/resources/token_public_key.pem").path)
        assertNotNull(keyPair)
    }
}

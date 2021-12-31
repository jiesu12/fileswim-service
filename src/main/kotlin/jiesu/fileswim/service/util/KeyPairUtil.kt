package jiesu.fileswim.service.util

import java.io.File
import java.security.KeyFactory
import java.security.KeyPair
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

/**
 * Generate a key:
 * ```
 * openssl genrsa -out token_private_key.pem 3072
 * ```
 *
 * Then convert the key to DER files for java to read.
 * ```
 * openssl pkcs8 -topk8 -inform PEM -outform DER -in token_private_key.pem -out token_private_key.der -nocrypt
 * openssl rsa -in token_private_key.pem -pubout -outform DER -out token_public_key.der
 * ```
 */
fun createKeyPair(privateFile: String, publicFile: String): KeyPair {
    val keyFactory = KeyFactory.getInstance("RSA")
    return KeyPair(
        keyFactory.generatePublic(X509EncodedKeySpec(File(publicFile).readBytes())),
        keyFactory.generatePrivate(PKCS8EncodedKeySpec(File(privateFile).readBytes()))
    )
}

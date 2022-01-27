package jiesu.service.util

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.util.io.pem.PemReader
import java.io.File
import java.io.FileReader
import java.security.KeyFactory
import java.security.KeyPair
import java.security.Security
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

/**
 * Generate private key in PKCS #1 PEM format:
 * ```
 * openssl genrsa -out tmp.pem 3072
 * ```
 * 
 * Convert PKCS #1 to PKCS #8:
 * ```
 * openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in tmp.pem -out token_private_key.pem
 * ```
 *
 * Create public key:
 * ```
 * openssl rsa -in token_private_key.pem -outform PEM -pubout -out token_public_key.pem
 * ```
 *
 * Convert PEM format to DER format.
 * ```
 * openssl pkcs8 -topk8 -inform PEM -outform DER -in token_private_key.pem -out token_private_key.der -nocrypt
 * openssl rsa -in token_private_key.pem -pubout -outform DER -out token_public_key.der
 * ```
 */
fun loadKeyPairFromDerFile(privateFile: String, publicFile: String): KeyPair {
    val keyFactory = KeyFactory.getInstance("RSA")
    return KeyPair(
        keyFactory.generatePublic(X509EncodedKeySpec(File(publicFile).readBytes())),
        keyFactory.generatePrivate(PKCS8EncodedKeySpec(File(privateFile).readBytes()))
    )
}

fun loadKeyPairFromPemFile(privateFile: String, publicFile: String): KeyPair =
    KeyPair(
        loadPublicKeyFromPemFile(File(publicFile)),
        loadPrivateKeyFromPemFile(File(privateFile))
    )

fun loadPublicKeyFromPemFile(file: File): RSAPublicKey {
    val factory = KeyFactory.getInstance("RSA")
    FileReader(file).use { keyReader ->
        PemReader(keyReader).use { pemReader ->
            val pemObject = pemReader.readPemObject()
            val content = pemObject.content
            val pubKeySpec = X509EncodedKeySpec(content)
            return factory.generatePublic(pubKeySpec) as RSAPublicKey
        }
    }
}

fun loadPrivateKeyFromPemFile(file: File): RSAPrivateKey {
    val factory = KeyFactory.getInstance("RSA")
    FileReader(file).use { keyReader ->
        PemReader(keyReader).use { pemReader ->
            val pemObject = pemReader.readPemObject()
            val content = pemObject.content
            val privateKeySpec = PKCS8EncodedKeySpec(content)
            return factory.generatePrivate(privateKeySpec) as RSAPrivateKey
        }
    }
}

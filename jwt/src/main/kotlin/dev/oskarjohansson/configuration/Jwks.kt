package dev.oskarjohansson.configuration

import java.lang.IllegalArgumentException

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import com.nimbusds.jose.jwk.RSAKey
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.token.TokenService
import org.springframework.stereotype.Component
import java.lang.IllegalStateException
import java.security.NoSuchAlgorithmException
import java.util.*

@Component
class Jwks {

    private val LOG: Logger = LoggerFactory.getLogger(Jwks::class.java)

    fun generateRSA(): RSAKey {

        return runCatching {
            val keypair = generateRsaKeyPair()
            val publicKey = keypair.public as RSAPublicKey
            val privateKey = keypair.private as RSAPrivateKey

            RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build()

        }.getOrElse {
            LOG.error("IllegalStateException in generateRSA(): $it")
            throw IllegalStateException("Error generating RSA Key Pair: ${it.message}")

        }

    }

    private fun generateRsaKeyPair(): KeyPair {

        return runCatching {
            val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
            keyPairGenerator.initialize(2048)
            keyPairGenerator.generateKeyPair()

        }.getOrElse {
            LOG.error("Illegal Argument Exception in generateRsaKeyPair: $it")
            throw IllegalArgumentException("Failed to generate RSA Key: ${it.message}")
        }

    }

}
package dev.oskarjohansson.configuration
import java.lang.IllegalArgumentException

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import com.nimbusds.jose.jwk.RSAKey
import org.springframework.stereotype.Component
import java.lang.IllegalStateException
import java.security.NoSuchAlgorithmException
import java.util.*

@Component
class Jwks {

    fun generateRSA(): RSAKey {

       return try {
           val keypair = generateRsaKeyPair()
           val publicKey = keypair.public as RSAPublicKey
           val privateKey = keypair.private as RSAPrivateKey

           return RSAKey.Builder(publicKey)
               .privateKey(privateKey)
               .keyID(UUID.randomUUID().toString())
               .build()

        } catch (ex: Exception ) {
            throw IllegalStateException("Error generating RSA Key Pair: ${ex.message}")

        }
    }

    private fun generateRsaKeyPair(): KeyPair {

        return try {
            val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
            keyPairGenerator.initialize(2048)
            keyPairGenerator.generateKeyPair()

        } catch (ex: Exception) {
            throw IllegalArgumentException("Failed to generate RSA Key: ${ex.message}")
        }
    }

}
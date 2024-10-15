
import java.lang.IllegalArgumentException

import java.security.KeyPair
import java.security.KeyPairGenerator


class Jwks {

    fun generateRsaKeyPair(): KeyPair {

        return try {
            val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
            keyPairGenerator.initialize(2048)
            keyPairGenerator.generateKeyPair()

        } catch (ex: Exception) {
            throw IllegalArgumentException(ex.message)
        }
    }

}
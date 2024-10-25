package dev.oskarjohansson.exceptions.service

import com.nimbusds.jose.jwk.JWK
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.security.interfaces.RSAPublicKey


@Service
class PublicKeyService() {

    private val LOG: Logger = LoggerFactory.getLogger(PublicKeyService::class.java)

    private val client = HttpClient(CIO)

    suspend fun getPublicKey(endpoint:String): RSAPublicKey {
        return runCatching {
            val response: String = client.get(endpoint).bodyAsText()
            val jwk = JWK.parse(response)

            LOG.debug("Retrieving public RSA key from: $endpoint")

            jwk.toRSAKey().toRSAPublicKey()
        }.getOrElse {
            LOG.error("Failed to retrieve public RSA key from: $endpoint, Stacktrace: \$it")
            throw IllegalStateException("Failed to retrieve RSA public key, ${it.message}")
        }
    }
}
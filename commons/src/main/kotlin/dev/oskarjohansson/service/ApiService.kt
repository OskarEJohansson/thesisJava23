package dev.oskarjohansson.service


import com.nimbusds.jose.jwk.JWK
import dev.oskarjohansson.model.LoginRequestDTO
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*

import io.ktor.client.statement.*
import io.ktor.http.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.security.interfaces.RSAPublicKey


@Service
class ApiService() {

    private val LOG: Logger = LoggerFactory.getLogger(ApiService::class.java)

    private val client = HttpClient(CIO)

    suspend fun getPublicKey(endpoint: String): RSAPublicKey {
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

    suspend fun loginUser(loginRequest: LoginRequestDTO): String {
        return runCatching {

            val response = client.post("http://localhost:8081/authentication/v1/login") {
                contentType(ContentType.Application.Json)
                setBody(loginRequest)
            }
            response.bodyAsText()
        }.getOrElse { exception ->
            LOG.error("Could not retrieve jwt token: ${exception.message}")

            when (exception) {
                is HttpRequestTimeoutException -> throw IllegalStateException("Login request timed out", exception)
                is ClientRequestException -> throw IllegalStateException("Invalid login credentials", exception)
                is ServerResponseException -> throw IllegalStateException("Server error during login", exception)
                else -> throw IllegalStateException("Unexpected error during login", exception)
            }
        }
    }
}
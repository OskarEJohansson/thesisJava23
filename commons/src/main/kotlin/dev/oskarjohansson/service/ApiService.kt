package dev.oskarjohansson.service


import com.nimbusds.jose.jwk.JWK
import dev.oskarjohansson.model.LoginRequestDTO
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import jakarta.validation.ValidationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.SocketTimeoutException
import java.security.interfaces.RSAPublicKey
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.*


@Service
class ApiService() {

    private val LOG: Logger = LoggerFactory.getLogger(ApiService::class.java)

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys=true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    suspend fun getPublicKey(): RSAPublicKey {
        return runCatching {
            val json = Json.parseToJsonElement(
                client.get("http://localhost:8081/public-key-controller/v1/public-key").bodyAsText()
            ).jsonObject

            val publicKey =
                (json["publicKey"])?.toString()
                    ?: throw IllegalStateException("Public RSA Key not found in the response from the RSA public key API ")

            return JWK.parse(publicKey).toRSAKey().toRSAPublicKey()

        }.getOrElse {
            LOG.error("Failed to retrieve public RSA key: Stacktrace: \n $it")
            throw IllegalStateException("Failed to retrieve RSA public key, ${it.message}")
        }
    }

    // TODO: Move to Library and turn return class into a Result
    suspend fun loginUser(loginRequest: LoginRequestDTO): String {
        return runCatching {

            val response = client.post("http://localhost:8081/authentication/v1/login") {
                contentType(ContentType.Application.Json)
                setBody(loginRequest)
            }.bodyAsText()

            val json = Json.parseToJsonElement(response).jsonObject

            json["token"]?.toString() ?: throw IllegalStateException("Failed to authenticate")
        }.getOrElse {
            LOG.error("Could not login user: ${it.message}, \n cause: ${it.cause}")
            when (it) {
                is UsernameNotFoundException, is ValidationException -> {
                    throw IllegalArgumentException("Error logging in due to user credentials")
                }

                is HttpRequestTimeoutException -> {
                    throw IllegalStateException("Request timed out: ${it.message}")
                }

                is SocketTimeoutException, is IOException -> {
                    throw IllegalStateException("Connection issue: ${it.message}")
                }

                else -> {
                    throw IllegalStateException("Failed to retrieve JWT token ${it.message}")
                }
            }
        }
    }
}
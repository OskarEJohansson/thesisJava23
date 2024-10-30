package dev.oskarjohansson.service


import com.nimbusds.jose.jwk.JWK
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
                ignoreUnknownKeys = true
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

}
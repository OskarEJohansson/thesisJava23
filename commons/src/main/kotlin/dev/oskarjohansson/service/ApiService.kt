package dev.oskarjohansson.service


import com.nimbusds.jose.jwk.JWK
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.interfaces.RSAPublicKey


@Service
class ApiService(
    private val httpClientService: HttpClientService,
@Value(value = "\${domain.host.address}") private val hostAddress:String
) {

    private val LOG: org.slf4j.Logger = LoggerFactory.getLogger(ApiService::class.java)

    suspend fun getPublicKey(): RSAPublicKey = runCatching {

            val json = Json.parseToJsonElement(
                httpClientService.client.get("${hostAddress}/public-key-controller/v1/public-key"
                ).bodyAsText()
            ).jsonObject

            val publicKey =
                (json["publicKey"])?.toString()
                    ?: throw IllegalStateException("Public RSA Key not found in the response from the RSA public key API ")

            JWK.parse(publicKey).toRSAKey().toRSAPublicKey()

        }.getOrElse {
            LOG.error("Failed to retrieve public RSA key: Stacktrace: \n ${it.message}, cause: ${it.cause}")
            throw IllegalStateException("Failed to retrieve RSA public key, ${it.message}")
        }
}
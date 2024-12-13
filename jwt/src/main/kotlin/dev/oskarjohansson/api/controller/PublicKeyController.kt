package dev.oskarjohansson.api.controller

import com.nimbusds.jose.jwk.RSAKey
import dev.oskarjohansson.api.dto.PublicKeyResponseDTO
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/public-key-controller")
class PublicKeyController(
    private val rsaKey: RSAKey
) {

    private val LOG: Logger = LoggerFactory.getLogger(PublicKeyController::class.java)

    @GetMapping("/v1/public-key")
    fun publicKey(request: HttpServletRequest): ResponseEntity<PublicKeyResponseDTO> {
        return runCatching {

            LOG.info("Request: ${request.method}")
            LOG.info("Headers: ${request.headerNames.toList()}")
            LOG.info("Parameters: ${request.parameterMap}")

            val publicKeyData = rsaKey.toPublicJWK().toJSONObject()
            val keyID = UUID.randomUUID().toString()

            LOG.info("KeyID: $keyID, PublicKeyData: $publicKeyData")

            ResponseEntity.ok().body(PublicKeyResponseDTO(keyID, publicKeyData))

        }.getOrElse {
            LOG.error("Error retrieving public RSA key: ${it.message}", it)
            throw IllegalStateException("Internal Server error: ${it.message}")
        }
    }

}


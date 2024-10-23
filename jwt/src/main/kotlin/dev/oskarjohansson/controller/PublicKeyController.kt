package dev.oskarjohansson.controller

import com.nimbusds.jose.jwk.RSAKey
import dev.oskarjohansson.domain.dto.PublicKeyResponseDTO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/public-key-controller")
class PublicKeyController(val rsaKey: RSAKey) {

    private val LOG: Logger = LoggerFactory.getLogger(PublicKeyController::class.java)

    @GetMapping("/v1/public-key")
    fun publicKey(): ResponseEntity<PublicKeyResponseDTO> =
        runCatching {
            val publicKeyData = rsaKey.toPublicJWK().toJSONObject()
            val keyID = UUID.randomUUID().toString()

            LOG.debug("KeyID: $keyID, PublicKeyData: $publicKeyData")

            ResponseEntity.ok(
                PublicKeyResponseDTO(keyID, publicKeyData)

            )
        }.getOrElse {
            LOG.error("Error retrieving public RSA key: ${it.message}", it)
            throw IllegalStateException("Internal Server error: ${it.message}")
        }

}


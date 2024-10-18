package dev.oskarjohansson.controller

import com.fasterxml.jackson.annotation.JsonKey
import com.nimbusds.jose.jwk.RSAKey
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PublicKeyController(val rsaKey: RSAKey) {

    // TODO: Connect logger 
    private val LOG: Logger = LoggerFactory.getLogger(PublicKeyController::class.java)

    // TODO: Find out why returntype myst be <out Any> and what it mean
    @GetMapping("/v1/public-key")
    fun publicKey(): ResponseEntity<out Any>? =
        runCatching {
            ResponseEntity.status(HttpStatus.OK)
                .body(
                    rsaKey.toPublicJWK().toJSONObject()
                )
        }.getOrElse {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal Server error: ")
        }

}


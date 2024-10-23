package dev.oskarjohansson.domain.dto

data class PublicKeyResponseDTO(val keyID: String, val publicKey: Map<String, Any>)

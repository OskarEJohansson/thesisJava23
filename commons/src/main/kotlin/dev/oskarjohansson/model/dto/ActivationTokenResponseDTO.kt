package dev.oskarjohansson.model.dto

import java.time.Instant

data class ActivationTokenResponseDTO(
    val email: String,
    val token: String,
    val expires: Instant
)
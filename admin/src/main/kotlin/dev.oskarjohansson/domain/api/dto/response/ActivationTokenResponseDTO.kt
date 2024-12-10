package dev.oskarjohansson.domain.api.dto.response

import java.time.Instant

data class ActivationTokenResponseDTO(
    val email: String,
    val token: String,
    val expires: Instant
)
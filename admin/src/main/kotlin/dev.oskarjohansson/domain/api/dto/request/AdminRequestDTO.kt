package dev.oskarjohansson.domain.api.dto.request

import org.springframework.validation.annotation.Validated

@Validated
data class AdminRequestDTO(
    val email: String,
    val username: String,
    val password: String
)

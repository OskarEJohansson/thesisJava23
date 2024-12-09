package dev.oskarjohansson.domain.api.dto.response

import dev.oskarjohansson.model.Role

data class AdminResponseDTO(
    val id: String,
    val email: String,
    val username: String,
    val role: Role,
)

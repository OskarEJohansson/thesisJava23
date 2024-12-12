package dev.oskarjohansson.api.dto

import dev.oskarjohansson.model.Role

data class UserResponseDTO(
    val id: String? = null,
    val email: String,
    val username: String,
    val role: Role,
)

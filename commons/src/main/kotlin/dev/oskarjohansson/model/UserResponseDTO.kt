package dev.oskarjohansson.model

data class UserResponseDTO(
    val id: String? = null,
    val email: String,
    val username: String,
    val role: Role,
)

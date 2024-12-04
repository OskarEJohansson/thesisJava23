package dev.oskarjohansson.api.dto.request

data class AdminRequestDTO(
    val email: String,
    val username: String,
    val password: String
)

package dev.oskarjohansson.config

data class AdminRequestDTO(
    val email: String,
    val username: String,
    val password: String
)

package dev.oskarjohansson.api.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.validation.annotation.Validated


@Validated
data class UserDTO(
    @Email val email: Email,
    @NotBlank(message = "Username must not be empty") @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters") val username: String,
    @NotBlank(message = "Password must not be blank") @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters") val password: String
)

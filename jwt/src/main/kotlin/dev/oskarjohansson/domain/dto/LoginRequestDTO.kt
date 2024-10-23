package dev.oskarjohansson.domain.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class LoginRequestDTO(@field:NotBlank(message = "Username must not be empty") val username: String,
                           @field:NotBlank(message = "Password must not be empty") val password: String) {

}
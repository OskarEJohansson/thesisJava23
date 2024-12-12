package dev.oskarjohansson.api.dto

import jakarta.validation.constraints.NotBlank
import kotlinx.serialization.Serializable
import org.springframework.validation.annotation.Validated

@Validated
@Serializable
data class LoginRequestDTO(@field:NotBlank(message = "Username must not be empty") val username: String,
                           @field:NotBlank(message = "Password must not be empty") val password: String) {

}
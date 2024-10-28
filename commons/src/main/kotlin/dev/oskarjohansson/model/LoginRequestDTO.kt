package dev.oskarjohansson.model

import jakarta.validation.constraints.NotBlank
import org.springframework.validation.annotation.Validated

@Validated
data class LoginRequestDTO(@field:NotBlank(message = "Username must not be empty") val username: String,
                           @field:NotBlank(message = "Password must not be empty") val password: String) {

}
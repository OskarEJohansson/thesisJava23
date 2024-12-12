package dev.oskarjohansson.api.dto


import org.springframework.validation.annotation.Validated

@Validated
data class ActivationTokenRequestDto(
    val activationToken: String
)

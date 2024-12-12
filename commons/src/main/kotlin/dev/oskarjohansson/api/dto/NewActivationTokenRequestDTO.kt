package dev.oskarjohansson.api.dto

import org.springframework.validation.annotation.Validated


@Validated
data class NewActivationTokenRequestDTO(
 val email:String
)

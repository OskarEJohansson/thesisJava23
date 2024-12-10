package dev.oskarjohansson.model

import dev.oskarjohansson.model.dto.ActivationTokenResponseDTO


fun ActivationToken.toActivationTokenResponseDTO(): ActivationTokenResponseDTO = ActivationTokenResponseDTO(
    this.email,
    this.token,
    this.expirationDate
)
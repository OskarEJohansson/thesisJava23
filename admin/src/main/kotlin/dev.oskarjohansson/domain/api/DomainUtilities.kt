package dev.oskarjohansson.domain.api

import dev.oskarjohansson.domain.api.dto.response.ActivationTokenResponseDTO
import dev.oskarjohansson.domain.api.dto.response.AdminResponseDTO
import dev.oskarjohansson.model.ActivationToken
import dev.oskarjohansson.model.User


fun User.toAdminResponseDTO(): AdminResponseDTO = AdminResponseDTO(
    this.id,
    this.email,
    this.username,
    this.role
)

fun ActivationToken.toActivationTokenResponseDTO(): ActivationTokenResponseDTO = ActivationTokenResponseDTO(
    this.email,
    this.token,
    this.expirationDate
)
package dev.oskarjohansson.domain.api

import dev.oskarjohansson.domain.api.dto.response.AdminResponseDTO
import dev.oskarjohansson.model.User


fun User.toAdminResponseDTO(): AdminResponseDTO = AdminResponseDTO(
    this.id,
    this.email,
    this.username,
    this.role
)
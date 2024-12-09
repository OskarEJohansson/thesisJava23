package dev.oskarjohansson.domain.api

import dev.oskarjohansson.domain.api.dto.response.AdminResponseDTO
import dev.oskarjohansson.model.User


fun User.toAdminResponseDTO(): AdminResponseDTO = AdminResponseDTO(
    this.id!!, //id is created when user is registered
    this.email,
    this.username,
    this.role
)
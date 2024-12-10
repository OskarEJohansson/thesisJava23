package dev.oskarjohansson.service

import dev.oskarjohansson.domain.api.dto.request.AdminRequestDTO
import dev.oskarjohansson.model.Role
import dev.oskarjohansson.model.User
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime


fun createAdminObject(user: AdminRequestDTO, passwordEncoder: PasswordEncoder): User = User(
    null,
    user.email,
    user.username,
    passwordEncoder.encode(user.password),
    Role.ADMIN,
    LocalDateTime.now(),
    null,
    false,
)


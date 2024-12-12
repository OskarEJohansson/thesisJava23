package dev.oskarjohansson.service

import dev.oskarjohansson.api.dto.request.UserRequestDTO
import dev.oskarjohansson.model.Role
import dev.oskarjohansson.model.User
import dev.oskarjohansson.api.dto.UserResponseDTO

import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime


fun createUserObject(user: UserRequestDTO, passwordEncoder: PasswordEncoder): User = User(
        null,
        user.email,
        user.username,
        passwordEncoder.encode(user.password),
        Role.USER,
        LocalDateTime.now(),
        null
    )

fun User.createUserResponseDTO(): UserResponseDTO = UserResponseDTO(
        this.id,
        this.email,
        this.username,
       this.role
)
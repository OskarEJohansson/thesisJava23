package dev.oskarjohansson.service

import dev.oskarjohansson.api.dto.UserDTO
import dev.oskarjohansson.domain.BookService
import dev.oskarjohansson.model.Role
import dev.oskarjohansson.model.User

import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime


fun UserService.createUserObject(user: UserDTO, passwordEncoder: PasswordEncoder): User {
    return User(
        null,
        user.email,
        user.username,
        passwordEncoder.encode(user.password),
        Role.USER,
        LocalDateTime.now(),
        null
    )
}

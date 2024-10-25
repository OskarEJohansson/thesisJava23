package dev.oskarjohansson.service

import dev.oskarjohansson.api.dto.UserDTO
import dev.oskarjohansson.domain.model.User
import dev.oskarjohansson.respository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.time.LocalDateTime

@Service
class UserService(private val userRepository: UserRepository, private val passwordEncoder: PasswordEncoder) {

    fun registerUser(userDTO: UserDTO): User {
        userRepository.findUserByUsernameOrEmail(userDTO.username, userDTO.email)
            ?.let { throw IllegalArgumentException("Username or Email already exist") }

        return userRepository.save(createUserObject(userDTO))
    }

    //TODO: Create a kotlin Extension to a saveUserFromDTO()
    fun createUserObject(user: UserDTO): User {
        return User(
            null, user.email, user.username, passwordEncoder.encode(user.password), TODO("Fix enum"), LocalDateTime.now(),
            null
        )
    }
}
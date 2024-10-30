package dev.oskarjohansson.service

import dev.oskarjohansson.api.dto.UserDTO
import dev.oskarjohansson.domain.model.User
import dev.oskarjohansson.respository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository, private val passwordEncoder: PasswordEncoder
) {

    fun registerUser(userDTO: UserDTO): User {
        userRepository.findUserByUsernameOrEmail(userDTO.username, userDTO.email)
            ?.let { throw IllegalArgumentException("Username or Email already exist") }

        return userRepository.save(createUserObject(userDTO, passwordEncoder))
    }

    fun getUsers(): List<User>{
        return userRepository.findAll() ?: throw IllegalStateException("Could not find any users")
    }
}
package dev.oskarjohansson.service

import dev.oskarjohansson.api.dto.UserDTO
import dev.oskarjohansson.domain.model.User
import dev.oskarjohansson.model.LoginRequestDTO
import dev.oskarjohansson.respository.UserRepository
import io.ktor.client.plugins.*
import jakarta.validation.ValidationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.SocketTimeoutException
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val apiService: ApiService
) {

    fun registerUser(userDTO: UserDTO): User {
        userRepository.findUserByUsernameOrEmail(userDTO.username, userDTO.email)
            ?.let { throw IllegalArgumentException("Username or Email already exist") }

        return userRepository.save(createUserObject(userDTO))
    }

    //TODO: Create a kotlin Extension to a saveUserFromDTO()
    fun createUserObject(user: UserDTO): User {
        return User(
            null,
            user.email,
            user.username,
            passwordEncoder.encode(user.password),
            User.Role.ROLE_USER,
            LocalDateTime.now(),
            null
        )
    }

    suspend fun loginUser(loginRequest: LoginRequestDTO): String {
        return runCatching {
            apiService.loginUser(loginRequest)

        }.getOrElse {
            when (it) {
                is UsernameNotFoundException, is ValidationException -> {
                    throw IllegalArgumentException("Error logging in due to user credentials")
                }

                is HttpRequestTimeoutException -> {
                    throw IllegalStateException("Request timed out: ${it.message}")
                }

                is SocketTimeoutException, is IOException -> {
                    throw IllegalStateException("Connection issue: ${it.message}")
                }

                else -> {
                    throw IllegalStateException("Failed to retrieve JWT token ${it.message}")
                }
            }
        }
    }
}
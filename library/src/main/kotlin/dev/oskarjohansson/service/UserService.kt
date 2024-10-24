package dev.oskarjohansson.service

import dev.oskarjohansson.domain.UserDTO
import dev.oskarjohansson.domain.model.User
import dev.oskarjohansson.respository.UserRepository
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.time.Instant

@Service
class UserService(private val userRepository: UserRepository) {

    fun registerUser(userDTO: UserDTO): User {
        userRepository.findByUsernameOrEmail(userDTO.username, userDTO.email)
            ?.let { throw IllegalArgumentException("Username or Email already exist") }

        return userRepository.save(createUserObject(userDTO))
    }

    //TODO: Create a kotlin Extension to a saveUserFromDTO()
    fun createUserObject(user: UserDTO): User {
        return User(
            null, user.email, user.username, TODO("Add password encoder"), TODO("Fix enum"), Instant.now(),
            null
        )
    }
}
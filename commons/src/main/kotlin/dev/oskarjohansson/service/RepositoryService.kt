package dev.oskarjohansson.service


import dev.oskarjohansson.model.User
import dev.oskarjohansson.repository.UserRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class RepositoryService(private val userRepository: UserRepository) {

    fun getUserByUsername(username: String): User {
        return userRepository.findByUsername(username) ?: throw UsernameNotFoundException(
            "Error retrieving user $username"
        )
    }
}
package dev.oskarjohansson.service

import dev.oskarjohansson.domain.entity.User
import dev.oskarjohansson.repository.UserRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class RepositoryService(private val userRepository: UserRepository) {

    fun getUserByUsername(username: String): User? {
      return runCatching {
            requireNotNull(userRepository.findbyUsername(username))
        }.getOrElse {
            throw UsernameNotFoundException("Error retrieving user", it)
        }
    }
}
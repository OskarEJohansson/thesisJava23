package dev.oskarjohansson.domain.service

import dev.oskarjohansson.model.User
import dev.oskarjohansson.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class AdminService(private val userRepository: UserRepository) {


    fun getUsers(): List<User>{

        return userRepository.findAll()

    }
}
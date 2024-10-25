package dev.oskarjohansson.respository

import dev.oskarjohansson.domain.model.User
import jakarta.validation.constraints.Email
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: MongoRepository<User, String> {

    fun findUserByUsernameOrEmail(username: String, email: Email):User?



}
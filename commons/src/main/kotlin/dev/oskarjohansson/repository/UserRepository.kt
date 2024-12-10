package dev.oskarjohansson.repository

import dev.oskarjohansson.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: MongoRepository<User, String> {

    fun findByUsername(username:String): User?
    fun findUserByUsernameOrEmail(username: String?, email: String?):User?
    fun findByEmail(email:String):User?
}
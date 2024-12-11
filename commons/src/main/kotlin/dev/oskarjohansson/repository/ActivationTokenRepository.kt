package dev.oskarjohansson.repository

import dev.oskarjohansson.model.ActivationToken
import org.springframework.data.mongodb.repository.MongoRepository

interface ActivationTokenRepository: MongoRepository<ActivationToken, String> {

    fun findByEmail(email:String):ActivationToken?
    fun findByToken(token:String):ActivationToken?
}
package dev.oskarjohansson.service

import dev.oskarjohansson.repository.UserRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class Adminservice(private val userRepository:UserRepository, private val passwordEncoder: PasswordEncoder) {


    val client = HttpClient(CIO){
        install(Logging){
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(ContentNegotiation){
            json(Json{
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    fun registerAuthor(){
        // TODO: write logic
    }

    suspend fun loginAdmin(){
        // TODO: write logic
    }

}
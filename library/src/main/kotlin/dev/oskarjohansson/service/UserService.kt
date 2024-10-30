package dev.oskarjohansson.service

import dev.oskarjohansson.api.dto.LoginRequestDTO
import dev.oskarjohansson.api.dto.UserDTO
import dev.oskarjohansson.model.User
import dev.oskarjohansson.repository.UserRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository, private val passwordEncoder: PasswordEncoder
) {

    private val LOG: Logger = LoggerFactory.getLogger(UserService::class.java)

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    fun registerUser(userDTO: UserDTO): User {
        userRepository.findUserByUsernameOrEmail(userDTO.username, userDTO.email)
            ?.let { throw IllegalArgumentException("Username or Email already exist") }

        return userRepository.save(createUserObject(userDTO, passwordEncoder))
    }

    fun getUsers(): List<User> {
        return userRepository.findAll() ?: throw IllegalStateException("Could not find any users")
    }

    suspend fun loginUser(loginRequestDTO: LoginRequestDTO): String {

        val response = runBlocking {
            client.post("http://localhost:8081/authentication/v1/login") {
                contentType(ContentType.Application.Json)
                setBody(loginRequestDTO)
            }
        }
        if (response.status.isSuccess()) {
            return Json.parseToJsonElement(response.bodyAsText()).jsonObject["token"]?.jsonPrimitive?.content
                ?: throw IllegalStateException("Could not log in user")
        } else {
            throw IllegalArgumentException("Error logging in, status code: ${response.status} ")
        }
    }
}



package dev.oskarjohansson.service

import dev.oskarjohansson.domain.api.dto.request.ActivationTokenRequestDTO
import dev.oskarjohansson.domain.api.dto.request.AdminRequestDTO
import dev.oskarjohansson.model.ActivationToken
import dev.oskarjohansson.model.User
import dev.oskarjohansson.model.dto.LoginRequestDTO
import dev.oskarjohansson.repository.ActivationTokenRepository
import dev.oskarjohansson.repository.UserRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepository: UserRepository,
    private val activationTokenRepository: ActivationTokenRepository,
    private val passwordEncoder: PasswordEncoder
) {

    val client = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    fun registerAdmin(adminRequestDTO: AdminRequestDTO): ActivationToken {
        userRepository.findUserByUsernameOrEmail(adminRequestDTO.username, adminRequestDTO.email)
            ?.let { throw IllegalStateException("Username or Email already exist") }

        val user = userRepository.save(
            createAdminObject(adminRequestDTO, passwordEncoder)
        ) ?: throw IllegalStateException("Could not save user")

        return ActivationToken(email = user.email)
    }

    fun activateUser(activationTokenRequestDTO: ActivationTokenRequestDTO): User {
        return run {

            val activationToken = activationTokenRepository.findByEmail(activationTokenRequestDTO.email)
                ?: throw IllegalStateException("Could not find token")

            val user = userRepository.findByEmail(activationTokenRequestDTO.email)
                ?: throw IllegalStateException("Could not find user")

            if (activationToken.token == activationTokenRequestDTO.token) {
                userRepository.save(user.copy(isEnabled = true))
            } else
                throw IllegalArgumentException("Could not activate user")
        }
    }


    suspend fun loginAdmin(loginRequestDTO: LoginRequestDTO): String {
        val response = runBlocking {
            client.post("http://jwt-service/authentication/v1/login") {
                contentType(ContentType.Application.Json)
                setBody(loginRequestDTO)
            }
        }
        if (response.status.isSuccess()) {
            return Json.parseToJsonElement(response.bodyAsText()).jsonObject["data"]?.jsonPrimitive?.content
                ?: throw IllegalStateException("Error parsing response from api, ${response.status}, \n request: ${response.request}")
        } else {
            throw IllegalArgumentException("Error logging in, status code: ${response.status} ")
        }
    }
}
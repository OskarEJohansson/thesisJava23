package dev.oskarjohansson.service

import dev.oskarjohansson.api.dto.request.LoginRequestDTO
import dev.oskarjohansson.api.dto.request.UserRequestDTO
import dev.oskarjohansson.model.User
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

import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository, private val passwordEncoder: PasswordEncoder
) {
    private val LOG: org.slf4j.Logger = LoggerFactory.getLogger(UserService::class.java)

    val client = HttpClient(CIO) {
        install(Logging){
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

    fun registerUser(userRequestDTO: UserRequestDTO): User {
        userRepository.findUserByUsernameOrEmail(userRequestDTO.username, userRequestDTO.email)
            ?.let { throw IllegalArgumentException("Username or Email already exist") }

        return userRepository.save(createUserObject(userRequestDTO, passwordEncoder))
    }


    //use "http://jwt-service/authentication/v1/login"
    // TODO: Set configMap? 
    suspend fun loginUser(loginRequestDTO: LoginRequestDTO): String {
        val response = runBlocking {
            client.post("http://jwt-service/authentication/v1/login") {
                contentType(ContentType.Application.Json)
                setBody(loginRequestDTO)
            }
        }

        if (response.status.isSuccess()) {
            LOG.debug("Response status for api call to login user,status: ${response.status.value}")
            return Json.parseToJsonElement(response.bodyAsText()).jsonObject["data"]?.jsonPrimitive?.content
                ?: throw IllegalStateException("Error parsing response from api, ${response.status}, \n request: ${response.request}")
        } else {
            throw IllegalArgumentException("Error logging in, status code: ${response.status} ")
        }
    }

    // TODO: Add activation service for user 
}



package dev.oskarjohansson.service

import dev.oskarjohansson.api.dto.NewActivationTokenRequestDTO
import dev.oskarjohansson.api.dto.request.LoginRequestDTO
import dev.oskarjohansson.api.dto.request.UserRequestDTO
import dev.oskarjohansson.domain.service.MailService
import dev.oskarjohansson.domain.service.UserActivationService
import dev.oskarjohansson.model.ActivationToken
import dev.oskarjohansson.repository.ActivationTokenRepository
import dev.oskarjohansson.repository.UserRepository
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val activationTokenRepository: ActivationTokenRepository,
    private val mailService: MailService,
    private val httpClientService: HttpClientService,
    private val userActivationService: UserActivationService,
    @Value(value = "\${domain.host.address}") private val hostAddress: String,
    @Value(value = "\${token.activation.address.library}") private val moduleAddress: String
) {
    private val LOG: org.slf4j.Logger = LoggerFactory.getLogger(UserService::class.java)

    fun registerUser(userRequestDTO: UserRequestDTO): Unit {
        userRepository.findUserByUsernameOrEmail(userRequestDTO.username, userRequestDTO.email)
            ?.let { throw IllegalArgumentException("Username or Email already exist") }

        val user = userRepository.save(
            createUserObject(userRequestDTO, passwordEncoder))
         ?: throw IllegalStateException("Could not save user")

        val activationToken: ActivationToken = activationTokenRepository.save(ActivationToken(email = user.email))
        LOG.debug("Token saved expires: ${activationToken.expirationDate}")

        return mailService.sendMail(activationToken.token, user.email,hostAddress, moduleAddress )
    }

    suspend fun loginUser(loginRequestDTO: LoginRequestDTO): String {
        val response = runBlocking {
            httpClientService.client.post("${hostAddress}/authentication/v1/login") {
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

    fun sendNewActivationToken(newActivationTokenRequestDto: NewActivationTokenRequestDTO): Unit {
        userActivationService.newActivationToken(newActivationTokenRequestDto, hostAddress, moduleAddress)
    }
}



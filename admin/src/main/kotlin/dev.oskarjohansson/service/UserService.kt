package dev.oskarjohansson.service

import dev.oskarjohansson.api.dto.LoginRequestDTO
import dev.oskarjohansson.api.dto.NewActivationTokenRequestDTO
import dev.oskarjohansson.domain.api.dto.request.AdminRequestDTO
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
    private val activationTokenRepository: ActivationTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val mailService: MailService,
    private val httpClientService: HttpClientService,
    private val userActivationService: UserActivationService,
    @Value(value = "\${domain.host.address}") private val hostAddress:String,
    @Value(value = "\${token.activation.address.admin}") private val moduleAddress:String
) {
    
    private val LOG:org.slf4j.Logger = LoggerFactory.getLogger(UserService::class.java)

    fun registerAdmin(adminRequestDTO: AdminRequestDTO): Unit {
        userRepository.findUserByUsernameOrEmail(adminRequestDTO.username, adminRequestDTO.email)
            ?.let { throw IllegalStateException("Username or Email already exist") }

        val user = userRepository.save(
            createAdminObject(adminRequestDTO, passwordEncoder)
        ) ?: throw IllegalStateException("Could not save user")

        val activationToken = activationTokenRepository.save(ActivationToken(email = user.email))

        return mailService.sendMail(activationToken.token, user.email, hostAddress, moduleAddress)
    }

    suspend fun loginAdmin(loginRequestDTO: LoginRequestDTO): String {
        LOG.info("Host address for Login Admin: $hostAddress")
        LOG.info("Module address for Login Admin, $moduleAddress")
        val response = runBlocking {
            httpClientService.client.post("${hostAddress}/authentication/v1/login") {
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

    fun sendNewActivationToken(newActivationTokenRequestDto: NewActivationTokenRequestDTO): Unit {
        userActivationService.newActivationToken(newActivationTokenRequestDto, hostAddress, moduleAddress)
    }
}
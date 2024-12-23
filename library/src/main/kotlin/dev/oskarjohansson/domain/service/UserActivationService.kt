package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.ActivationTokenRequestDto
import dev.oskarjohansson.api.dto.NewActivationTokenRequestDTO
import dev.oskarjohansson.model.ActivationToken
import dev.oskarjohansson.model.User
import dev.oskarjohansson.repository.ActivationTokenRepository
import dev.oskarjohansson.repository.UserRepository
import dev.oskarjohansson.service.tokenPattern
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class UserActivationService(
    private val userRepository: UserRepository,
    private val activationTokenRepository: ActivationTokenRepository,
    private val mailService: MailService
) {

    fun newActivationToken(newActivationTokenRequest: NewActivationTokenRequestDTO, hostAddress:String, moduleAddress:String) {

        val user =
            userRepository.findByEmail(newActivationTokenRequest.email) ?: throw IllegalStateException("User not found")

        user.takeIf { !user.isEnabled }
            ?.let {
                val token = activationTokenRepository.save(ActivationToken(email = user.email))
                mailService.sendMail(token.token, token.email, hostAddress, moduleAddress)

            } ?: throw IllegalStateException("User is already activated")
    }

    fun activateUser(activationToken: ActivationTokenRequestDto): User {

        tokenPattern(activationToken.activationToken).takeIf { !it }
            ?.let { throw IllegalArgumentException("Activation token is not valid") }

        return run {

            val activationToken: ActivationToken =
                activationTokenRepository.findByToken(activationToken.activationToken)
                    ?: throw IllegalStateException("Could not find token")
            val user = userRepository.findByEmail(activationToken.email)
                ?: throw IllegalStateException("Could not find user")

            if (!user.isEnabled) {
                userRepository.save(user.copy(isEnabled = true))
            } else {
                throw IllegalStateException("User is activated")
            }
        }
    }
}
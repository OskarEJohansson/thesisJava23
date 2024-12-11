package dev.oskarjohansson.service

import dev.oskarjohansson.model.ActivationToken
import dev.oskarjohansson.model.User
import dev.oskarjohansson.model.dto.ActivationTokenRequestDTOTEST
import dev.oskarjohansson.model.dto.ActivationTokenRequestDto
import dev.oskarjohansson.model.dto.NewActivationTokenRequestDTO
import dev.oskarjohansson.repository.ActivationTokenRepository
import dev.oskarjohansson.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserActivationService(private val userRepository: UserRepository,private val activationTokenRepository: ActivationTokenRepository,private val mailService: MailService) {

    // TODO: Implement email delivery
    fun newActivationToken(newActivationTokenRequest: NewActivationTokenRequestDTO): Unit {
        val user =
            userRepository.findByEmail(newActivationTokenRequest.email) ?: throw IllegalStateException("User not found")

        user.takeIf { !user.isEnabled }
            ?.let {
                val token = activationTokenRepository.save(ActivationToken(email = user.email))
                mailService.sendMail(token.token ,token.email)

            } ?: throw IllegalStateException("User is already activated")
    }

    fun activateUserTEST(activationTokenRequestDTOTEST: ActivationTokenRequestDTOTEST): User {
        return run {

            val activationToken = activationTokenRepository.findByEmail(activationTokenRequestDTOTEST.email)
                ?: throw IllegalStateException("Could not find token")

            val user = userRepository.findByEmail(activationTokenRequestDTOTEST.email)
                ?: throw IllegalStateException("Could not find user")

            if (activationToken.token == activationTokenRequestDTOTEST.token) {
                userRepository.save(user.copy(isEnabled = true))
            } else
                throw IllegalArgumentException("Could not activate user")
        }
    }

    fun activateUser(activationToken: ActivationTokenRequestDto): User {
        return run {

            val activationToken: ActivationToken = activationTokenRepository.findByToken(activationToken.activationToken)
                ?: throw IllegalStateException("Could not find token")

            val user = userRepository.findByEmail(activationToken.email)
                ?: throw IllegalStateException("Could not find user")

            if(!user.isEnabled){
                userRepository.save(user.copy(isEnabled = true))
            } else {
                throw IllegalStateException("User is activated")
            }
        }
    }
}
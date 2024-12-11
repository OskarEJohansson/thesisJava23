package dev.oskarjohansson.service

import dev.oskarjohansson.model.ActivationToken
import dev.oskarjohansson.model.User
import dev.oskarjohansson.model.dto.ActivationTokenRequestDTOTEST
import dev.oskarjohansson.model.dto.NewActivationTokenRequestDTO
import dev.oskarjohansson.repository.ActivationTokenRepository
import dev.oskarjohansson.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserActivationService(val userRepository: UserRepository, val activationTokenRepository: ActivationTokenRepository) {

    // TODO: Implement email delivery
    fun newActivationToken(newActivationTokenRequest: NewActivationTokenRequestDTO): ActivationToken {
        val user =
            userRepository.findByEmail(newActivationTokenRequest.email) ?: throw IllegalStateException("User not found")

        user.takeIf { !user.isEnabled }
            ?.let {
                return activationTokenRepository.save(ActivationToken(email = user.email))
            } ?: throw IllegalStateException("User is already activated")
    }

    // TODO: move to common
    fun activateUser(activationTokenRequestDTOTEST: ActivationTokenRequestDTOTEST): User {
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

}
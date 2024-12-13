package dev.oskarjohansson.service

import dev.oskarjohansson.api.dto.request.UserRequestDTO
import dev.oskarjohansson.repository.ActivationTokenRepository
import dev.oskarjohansson.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.password.PasswordEncoder

class UserServiceTest {

    private val encoder = mockk<PasswordEncoder>()
    private val repository = mockk<UserRepository>()
    private val activationTokenRepositor = mockk<ActivationTokenRepository>()
    private val mailService = mockk<MailService>()
    private val httpClientService = mockk<HttpClientService>()
    private val hostAddress = mockk<String>()
    private val userService =
        UserService(repository, encoder, activationTokenRepositor, mailService, httpClientService, hostAddress)


//    @Test
//    fun `Test that register user throws IllegalArgumentException when trying to register a username that already exist`(){
//// TODO: implement test
//}
}
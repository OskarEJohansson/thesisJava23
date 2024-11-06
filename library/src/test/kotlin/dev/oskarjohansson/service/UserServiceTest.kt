package dev.oskarjohansson.service

import dev.oskarjohansson.api.dto.UserDTO
import dev.oskarjohansson.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.password.PasswordEncoder

class UserServiceTest {

    private val encoder = mockk<PasswordEncoder>()
    private val repository = mockk<UserRepository>()
    private val userService = UserService(repository,encoder)


    @Test
    fun `Test that register user throws IllegalArgumentException when trying to register a username that already exist`(){

        every { repository.findUserByUsernameOrEmail(any(),any()) } returns mockk()
        assertThrows<IllegalArgumentException> { userService.registerUser(UserDTO("123","123","123"))  }

    }
}
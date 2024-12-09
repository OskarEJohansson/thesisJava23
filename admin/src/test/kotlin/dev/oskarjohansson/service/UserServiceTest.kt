package dev.oskarjohansson.service

import dev.oskarjohansson.domain.api.dto.request.AdminRequestDTO
import dev.oskarjohansson.repository.UserRepository

import io.mockk.every

import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.password.PasswordEncoder

class UserServiceTest {

    private val repository = mockk<UserRepository>()
    private val encoder = mockk<PasswordEncoder>()
    private val userService = UserService(repository, encoder)


    @Test
    fun`test that registerAdmin throws error when an admin user already exist`(){
        every { repository.findUserByUsernameOrEmail(any(), any()) } returns mockk()
        assertThrows<IllegalArgumentException> { userService.registerAdmin(AdminRequestDTO("test@mail.com", "admin", "pass"))  }
    }
}
package dev.oskarjohansson.service

import dev.oskarjohansson.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.UsernameNotFoundException

@SpringBootTest
@AutoConfigureMockMvc
class UserDetailServiceTest(@Autowired private val userDetailService: UserDetailService) {

    @Test
    fun `test global exception handling for UserNotFound working`() {

        val mockRepo = mockk<UserRepository>()
        every { mockRepo.findByUsername(any()) } returns null

        assertThrows<UsernameNotFoundException> {
            userDetailService.loadUserByUsername("user")
        }
    }
}
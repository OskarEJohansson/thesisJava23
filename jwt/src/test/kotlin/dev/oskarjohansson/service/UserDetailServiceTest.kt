package dev.oskarjohansson.service

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserDetailServiceTest() {

    private val repository = mockk<RepositoryService>()
    private val userDetailService = UserDetailService(repository)

    @Test
    fun `test global exception handling for UserNotFound working`() {

        every { repository.getUserByUsername(any()) } returns mockk()

        assertThrows<UsernameNotFoundException> {
            userDetailService.loadUserByUsername("user")
        }
    }
}
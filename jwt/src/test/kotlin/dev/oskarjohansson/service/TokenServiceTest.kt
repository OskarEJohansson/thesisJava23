package dev.oskarjohansson.service



import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.Authentication
import kotlin.test.Test
import kotlin.test.assertTrue


@SpringBootTest
@AutoConfigureMockMvc
class TokenServiceTest @Autowired constructor(private val tokenservice: TokenService) {

    @Test
    fun `test that token is returned when user is authenticated`() {
        val mockAuth = mockk<Authentication>()
        every{mockAuth.isAuthenticated } returns true
        every{mockAuth.name } returns "User"
        every{mockAuth.credentials } returns "ROLE_USER"
        val token = tokenservice.generateToken(mockAuth)

        assertTrue(token.isNotEmpty())
    }
}

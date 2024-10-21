package dev.oskarjohansson.service



import dev.oskarjohansson.configuration.SecurityConfiguration
import io.mockk.InternalPlatformDsl.toStr
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.config.core.GrantedAuthorityDefaults
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.JwtDecoder
import kotlin.test.Test
import kotlin.test.assertTrue


@SpringBootTest
@AutoConfigureMockMvc
class TokenServiceTest @Autowired constructor(private val tokenService: TokenService, private val securityConfiguration: SecurityConfiguration) {

    @Test
    fun `test that token is returned when user is authenticated`() {
        val mockAuth = mockk<Authentication>()
        every{mockAuth.isAuthenticated } returns true
        every{mockAuth.name } returns "User"
        every{mockAuth.credentials } returns "User"
        every { mockAuth.authorities } returns listOf(SimpleGrantedAuthority("ROLE_USER"))
        val token = tokenService.generateToken(mockAuth)

        assertTrue(token.isNotEmpty())
    }
}

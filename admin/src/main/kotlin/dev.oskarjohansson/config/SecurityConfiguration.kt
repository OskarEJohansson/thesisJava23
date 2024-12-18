package dev.oskarjohansson.config

import dev.oskarjohansson.service.ApiService
import io.ktor.client.plugins.*
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain
import java.io.IOException
import java.net.SocketTimeoutException
import java.security.interfaces.RSAPublicKey

@Configuration
@EnableWebSecurity
class SecurityConfiguration(private val apiService: ApiService) {

    private val LOG: org.slf4j.Logger = LoggerFactory.getLogger(SecurityConfiguration::class.java)

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun getPublicKeyFromTokenService(): RSAPublicKey {
        return runCatching {
            runBlocking { apiService.getPublicKey()
            }
        }.getOrElse {
            when (it) {
                is HttpRequestTimeoutException -> {
                    LOG.error("Failed to retrieve RSA public key: ${it.message}, Stacktrace: ${it.cause}")
                    throw IllegalStateException("Request timed out: ${it.message}")
                }

                is SocketTimeoutException, is IOException -> {
                    LOG.error("Failed to retrieve RSA public key: ${it.message}, Stacktrace: ${it.cause}")
                    throw IllegalStateException("Connection issue: ${it.message}")
                }

                else -> {
                    LOG.error("Failed to retrieve RSA public key: ${it.message}, Stacktrace: ${it.cause}")
                    throw IllegalStateException("Failed to retrieve RSA public key ${it.message}")
                }
            }
        }
    }

    @Bean
    fun jwtDecoder(rsaPublicKey: RSAPublicKey): JwtDecoder {
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build()
    }


    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        return http
            .csrf { it.disable() }
            .oauth2ResourceServer { it.jwt(Customizer.withDefaults()) }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers("/admin/authentication/v1/login", "/admin/authentication/v1/create-admin" ,"/admin/authentication/v1/activate-account/*", "/health").permitAll()
                it.requestMatchers("/v3/*","/v3/api-docs/swagger-config", "/swagger-ui/*").permitAll() // TODO: Find out how to to do this security chain safe when using a browser
                it.requestMatchers("/admin/secured/v1/**").hasAuthority("SCOPE_ROLE_ADMIN")
                it.anyRequest().authenticated()
            }
            .build()
    }
}
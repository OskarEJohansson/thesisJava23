package dev.oskarjohansson.configuration

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
import java.lang.IllegalStateException
import java.security.interfaces.RSAPublicKey
import java.io.IOException
import java.net.SocketTimeoutException

@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    private val LOG: org.slf4j.Logger = LoggerFactory.getLogger(SecurityConfiguration::class.java)

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun getPublicKeyFromTokenService(): RSAPublicKey {
        return runCatching {
            runBlocking { ApiService().getPublicKey() }
        }.getOrElse {
            LOG.error("Failed to retrieve RSA public key: ${it.message}, Stacktrace: ${it}")
            when (it) {
                is HttpRequestTimeoutException -> {
                    throw IllegalStateException("Request timed out: ${it.message}")
                }

                is SocketTimeoutException, is IOException -> {
                    throw IllegalStateException("Connection issue: ${it.message}")
                }

                else -> {
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
                it.requestMatchers("/user/v1/register-user", "/user/v1/login").permitAll()
                it.anyRequest().authenticated() }
            .build()
    }


}
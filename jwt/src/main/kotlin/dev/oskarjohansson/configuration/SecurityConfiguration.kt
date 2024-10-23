package dev.oskarjohansson.configuration

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.*
import org.springframework.security.web.SecurityFilterChain
import org.springframework.stereotype.Component

@Configuration
@Component
@EnableWebSecurity
class SecurityConfiguration {

    private val LOG: Logger = LoggerFactory.getLogger(SecurityConfiguration::class.java)

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(userDetailsService: UserDetailsService): AuthenticationManager {

        return ProviderManager(
            DaoAuthenticationProvider().apply {
                setUserDetailsService(userDetailsService)
                setPasswordEncoder(passwordEncoder())
            }
        )
    }

    @Bean
    fun generateRsaKey(): RSAKey = Jwks().generateRSA()

    // TODO: Make sure the JWKSet(rsaKey) does not need .key
    @Bean
    fun jwksSource(rsaKey: RSAKey): JWKSource<SecurityContext> =
        JWKSource { jwkSelector, _ -> jwkSelector.select(JWKSet(rsaKey)) }

    @Bean
    fun jwtEncoder(jwks: JWKSource<SecurityContext>): JwtEncoder = NimbusJwtEncoder(jwks)

    @Bean
    fun jwtDecoder(rsaKey: RSAKey): JwtDecoder =
        runCatching {
            NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build()
        }.getOrElse {
            LOG.error("Error decoding JWT: $it")
            throw JwtDecoderInitializationException("Error decoding the JWT: ${it.message}", it)
        }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        // TODO: Set up proper security filter chain 

        return http
            .csrf { csrf -> csrf.disable() }
            .sessionManagement { sesssion -> sesssion.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/public-key-controller/v1/public-key").permitAll()
                    .requestMatchers("/auth-controller/v1/request-token").authenticated()
                    .anyRequest().authenticated()
            }
            .build()
    }
}
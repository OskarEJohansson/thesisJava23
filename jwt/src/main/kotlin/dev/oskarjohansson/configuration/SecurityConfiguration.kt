package dev.oskarjohansson.configuration

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
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
        JWKSource { jwkSelector, context -> jwkSelector.select(JWKSet(rsaKey)) }

    @Bean
    fun jwtEncoder(jwks: JWKSource<SecurityContext>): JwtEncoder = NimbusJwtEncoder(jwks)

    @Bean
    fun jwtDecoder(rsaKey: RSAKey): JwtDecoder =
        runCatching {
            NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build()
        }.getOrElse { ex ->
            throw throw JwtDecoderInitializationException("Error decoding the JWT", ex)
        }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        // TODO: Set up proper security filter chain 
        
        return http
            .csrf{ csrf -> csrf.disable()}
            .sessionManagement{ sesssion -> sesssion.sessionCreationPolicy(SessionCreationPolicy.STATELESS)}
            .authorizeHttpRequests { auth -> auth
                .requestMatchers("/*").permitAll()}
            .build()
    }
}
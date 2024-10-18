package dev.oskarjohansson.service

import dev.oskarjohansson.domain.entity.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.token.TokenService
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors


@Service
class TokenService(private val jwtEncoder: JwtEncoder) {

    private val LOG:Logger = LoggerFactory.getLogger(TokenService::class.java)


    fun generateToken(authentication: Authentication): String {

        val now = Instant.now()

        val scope = authentication.authorities
            .joinToString(" ") { grantedAuthority -> grantedAuthority.authority  }

         val claims:JwtClaimsSet = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(1, ChronoUnit.HOURS))
            .subject(authentication.name)
            .claim("scope", scope)
            .build()

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).tokenValue

    }

}
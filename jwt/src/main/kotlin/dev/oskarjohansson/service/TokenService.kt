package dev.oskarjohansson.service

import dev.oskarjohansson.model.CustomUserDetails
import kotlinx.serialization.MissingFieldException

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.token.TokenService
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.lang.IllegalStateException
import java.time.Instant
import java.time.temporal.ChronoUnit


@Service
class TokenService(private val jwtEncoder: JwtEncoder) {

    private val LOG:Logger = LoggerFactory.getLogger(TokenService::class.java)

    fun generateToken(authentication: Authentication): String {

        val now = Instant.now()

        val scope = authentication.authorities
            .joinToString(" ") { grantedAuthority -> grantedAuthority.authority  }

        val customUserDetails = authentication.principal as? CustomUserDetails

        if (customUserDetails != null) {
            LOG.debug("User ID in principal: ${customUserDetails.getUserId()}")
        }

//        TODO("Add audience, check out clock and add logging and move variables to properties as env")
         val claims:JwtClaimsSet = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(1, ChronoUnit.HOURS))
            .subject(authentication.name)
            .claim("scope", scope)
            .claim("userId", customUserDetails?.getUserId() ?: throw IllegalStateException("Could not find userID in JwtClaimsSet Builder"))
            .build()


        LOG.info("Token generated for${authentication.name}, authorities granted: ${authentication.authorities}, claim: ${claims.toString()}")
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).tokenValue

    }
}
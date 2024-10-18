package dev.oskarjohansson.service

import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.stereotype.Service


@Service
class TokenService(jwtEncoder: JwtEncoder) {



    fun generateToken(authentication: Authentication): String {
        TODO("Implement logger, time, scope and JWTClaimSet")
        return "Token"
    }

}
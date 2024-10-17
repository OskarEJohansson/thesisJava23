package dev.oskarjohansson.controller

import dev.oskarjohansson.domain.entity.LoginRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class AuthController(
    private val tokenService: dev.oskarjohansson.service.TokenService,
    private val authenticationManager: AuthenticationManager
) {
    
    private val LOG: Logger = LoggerFactory.getLogger(AuthController::class.java)
    // TODO: Ask Johan regarding logging in a controller 

    @PostMapping("/v1/request-token")
    fun token(loginRequest: LoginRequest): ResponseEntity<String> =

        runCatching {
            val auth: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
            )
            ResponseEntity.ok(tokenService.generateToken(auth))
        }.getOrElse {
            ResponseEntity.badRequest().build()
        }


}

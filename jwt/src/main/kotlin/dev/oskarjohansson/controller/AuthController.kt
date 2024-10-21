package dev.oskarjohansson.controller

import dev.oskarjohansson.domain.entity.LoginRequest
import dev.oskarjohansson.service.TokenService
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class AuthController(
    private val tokenService: TokenService,
    private val authenticationManager: AuthenticationManager
) {
    
    private val LOG: Logger = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/v1/request-token")
    fun token(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<String> =

        runCatching {
            LOG.debug("Token request with login Request Username: ${loginRequest.username}")
            val auth: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
            )
            LOG.debug("User Authenticated: ${auth.name}")
            //todo: "Make sure caller cam read the token. Turn it into a hashmap?"
            ResponseEntity.status(HttpStatus.OK).body("Login Successful, token: ${tokenService.generateToken(auth)}")
        }.getOrElse {
            ResponseEntity.badRequest().build()
        }

}

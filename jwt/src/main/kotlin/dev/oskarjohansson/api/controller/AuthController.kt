package dev.oskarjohansson.api.controller

import dev.oskarjohansson.api.dto.TokenResponseDTO
import dev.oskarjohansson.model.LoginRequestDTO
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
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/authentication")
class AuthController(
    private val tokenService: TokenService,
    private val authenticationManager: AuthenticationManager
) {

    private val LOG: Logger = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/v1/login")
    fun token(@Valid @RequestBody loginRequestDTO: LoginRequestDTO): ResponseEntity<TokenResponseDTO> =

        runCatching {
            LOG.debug("Token request with login Request Username: ${loginRequestDTO.username}")
            println("LOGINREQUESTDTO: $loginRequestDTO")
            val auth: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequestDTO.username, loginRequestDTO.password)
            )
            LOG.debug("User Authenticated: ${auth.name}")
            //todo: "Make sure caller can read the token. Turn it into a hashmap?"
            ResponseEntity.ok(TokenResponseDTO("Login Successful", tokenService.generateToken(auth)))
        }.getOrElse {
            LOG.error("Failed to authenticate: ${it.message}")
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(TokenResponseDTO("Invalid login credentials", ""))
        }

}

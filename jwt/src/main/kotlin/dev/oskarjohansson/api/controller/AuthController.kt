package dev.oskarjohansson.api.controller


import dev.oskarjohansson.api.dto.LoginRequestDTO
import dev.oskarjohansson.api.dto.ResponseDTO
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
    private val authenticationManager: AuthenticationManager,
) {

    private val LOG: Logger = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/v1/login")
    fun token(@Valid @RequestBody loginRequestDTO: LoginRequestDTO): ResponseEntity<ResponseDTO<String>> =

        runCatching {
            val auth: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequestDTO.username, loginRequestDTO.password)
            )
            LOG.debug("User Authenticated: ${auth.name}")
            ResponseEntity.ok(ResponseDTO(HttpStatus.OK.value(), message = "Login Successful", tokenService.generateToken(auth)))

        }.getOrElse {

            LOG.error("Failed to authenticate: ${it.message}, ${it.printStackTrace()}")
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO(HttpStatus.BAD_REQUEST.value() ,"${it.localizedMessage}" ))
        }
}

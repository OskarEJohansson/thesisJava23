package dev.oskarjohansson.api

import dev.oskarjohansson.api.dto.request.LoginRequestDTO
import dev.oskarjohansson.api.dto.request.UserRequestDTO
import dev.oskarjohansson.model.dto.*
import dev.oskarjohansson.model.toActivationTokenResponseDTO
import dev.oskarjohansson.service.UserActivationService
import dev.oskarjohansson.service.UserService
import dev.oskarjohansson.service.createUserResponseDTO

import jakarta.validation.Valid
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(private val userService: UserService, private val userActivationService: UserActivationService) {

    private val LOG: Logger = LoggerFactory.getLogger(UserController::class.java)

    @PostMapping("/v1/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequestDTO): ResponseEntity<ResponseDTO<String>> {
        return runCatching {
            ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO(HttpStatus.OK.value(), "Login Successful", runBlocking {
                    userService.loginUser(loginRequest)
                }))
        }.getOrElse {
            ResponseEntity.badRequest()
                .body(ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Could not login user: ${it.message}"))
        }
    }

    @PostMapping("/v1/register-user")
    fun registerUser(@Valid @RequestBody registerUser: UserRequestDTO): ResponseEntity<ResponseDTO<ActivationTokenResponseDTO>> {

        return runCatching {
            ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO(HttpStatus.OK.value(), "User successfully registered", userService.registerUser(registerUser).toActivationTokenResponseDTO()))
        }.getOrElse {
            ResponseEntity.badRequest().body(ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Could not register user, ${it.message}"))
        }
    }


    @PostMapping("/v1/activate-account")
    fun activate(@Validated @RequestBody activationTokenRequestDTOTEST: ActivationTokenRequestDTOTEST): ResponseEntity<ResponseDTO<UserResponseDTO>> {
        return runCatching {
            ResponseEntity.status(HttpStatus.OK).body(
                ResponseDTO(
                    HttpStatus.OK.value(),
                    "Account activate",
                    userActivationService.activateUser(activationTokenRequestDTOTEST).createUserResponseDTO()
                )
            )
        }.getOrElse {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTO(HttpStatus.BAD_REQUEST.value(),
                    "${it.message}"))
        }
    }

    @PostMapping("/v1/send-new-activation-token")
    fun sendNewActivationToken(@Validated @RequestBody email: NewActivationTokenRequestDTO): ResponseEntity<ResponseDTO<ActivationTokenResponseDTO>> {
        return runCatching {
            ResponseEntity.status(HttpStatus.OK).body(
                ResponseDTO(
                    HttpStatus.OK.value(),
                    "New activation token",
                    userActivationService.newActivationToken(email).toActivationTokenResponseDTO()
                )
            )
        }.getOrElse {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    "${it.message}"
                )
            )
        }
    }




}
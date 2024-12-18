package dev.oskarjohansson.domain.api


import dev.oskarjohansson.api.dto.ActivationTokenRequestDto
import dev.oskarjohansson.api.dto.LoginRequestDTO
import dev.oskarjohansson.api.dto.NewActivationTokenRequestDTO
import dev.oskarjohansson.api.dto.ResponseDTO
import dev.oskarjohansson.domain.api.dto.request.AdminRequestDTO
import dev.oskarjohansson.domain.api.dto.response.AdminResponseDTO
import dev.oskarjohansson.service.UserActivationService
import dev.oskarjohansson.service.UserService
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/admin/authentication/v1")
class AuthenticationController(private val userService: UserService, private val userActivationService: UserActivationService) {


    @PostMapping("/login")
    fun login(@Validated @RequestBody loginRequestDTO: LoginRequestDTO): ResponseEntity<ResponseDTO<String>> {
        return runCatching {
            ResponseEntity.status(HttpStatus.OK).body(
                ResponseDTO(
                    HttpStatus.OK.value(),
                    "Login successful",
                    runBlocking { userService.loginAdmin(loginRequestDTO) })
            )
        }.getOrElse {
            ResponseEntity.badRequest()
                .body(
                    ResponseDTO(HttpStatus.BAD_REQUEST.value(),
                    "Could not login user: ${it.message}")
                )
        }
    }

    @PostMapping("/create-admin")
    fun create(@Validated @RequestBody adminRequestDTO: AdminRequestDTO): ResponseEntity<ResponseDTO<Unit>> {
        return runCatching {

            ResponseEntity.status(HttpStatus.OK).body(
                ResponseDTO(
                    HttpStatus.OK.value(),
                    "Admin user created",
                    userService.registerAdmin(adminRequestDTO)
                )
            )
        }.getOrElse {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    ResponseDTO(HttpStatus.BAD_REQUEST.value(),
                    "${it.message}")
                )
        }
    }

    @PostMapping("/send-new-activation-token")
    fun sendNewActivationToken(@Validated @RequestBody email: NewActivationTokenRequestDTO): ResponseEntity<ResponseDTO<Unit>> {
        return runCatching {
            ResponseEntity.status(HttpStatus.OK).body(
                ResponseDTO(
                    HttpStatus.OK.value(),
                    "New activation token",
                    userService.sendNewActivationToken(email)
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

    @PostMapping("/activate-account/{activationToken}")
    fun activateAdmin(@PathVariable activationToken: ActivationTokenRequestDto): ResponseEntity<ResponseDTO<AdminResponseDTO>>{

        return runCatching {
            ResponseEntity.status(HttpStatus.OK).body(
                ResponseDTO(
                    HttpStatus.OK.value(),
                    "Account activated",
                    userActivationService.activateUser(activationToken).toAdminResponseDTO()
                )
            )
        }.getOrElse {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    "${it.message}",
                )
            )
        }
    }
}
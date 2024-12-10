package dev.oskarjohansson.domain.api

import dev.oskarjohansson.domain.api.dto.request.ActivationTokenRequestDTO
import dev.oskarjohansson.domain.api.dto.request.AdminRequestDTO
import dev.oskarjohansson.domain.api.dto.response.ActivationTokenResponseDTO
import dev.oskarjohansson.domain.api.dto.response.AdminResponseDTO
import dev.oskarjohansson.model.dto.LoginRequestDTO
import dev.oskarjohansson.model.dto.ResponseDTO
import dev.oskarjohansson.model.User
import dev.oskarjohansson.service.UserService
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/admin")
class AdminController(private val adminService: UserService) {

    @GetMapping("/v1/users")
    fun users(): ResponseEntity<ResponseDTO<List<User>>> {

        return try {
            ResponseEntity.status(HttpStatus.OK).body(ResponseDTO(HttpStatus.OK.value(), "Users"))
        } catch (exception: IllegalStateException) {

            ResponseEntity.internalServerError().body(
                ResponseDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error getting users, ${exception.message}"
                )
            )
        }
    }

    @PostMapping("/v1/login")
    fun login(@Validated @RequestBody loginRequestDTO: LoginRequestDTO): ResponseEntity<ResponseDTO<String>> {
        return runCatching {
            ResponseEntity.status(HttpStatus.OK).body(
                ResponseDTO(
                    HttpStatus.OK.value(),
                    "Login successful",
                    runBlocking { adminService.loginAdmin(loginRequestDTO) }))
        }.getOrElse {
            ResponseEntity.badRequest()
                .body(ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Could not login user: ${it.message}"))
        }
    }

    @PostMapping("/v1/create-admin")
    fun create(@Validated @RequestBody adminRequestDTO: AdminRequestDTO): ResponseEntity<ResponseDTO<ActivationTokenResponseDTO>> {
        return runCatching {
            ResponseEntity.status(HttpStatus.OK).body(
                ResponseDTO(
                    HttpStatus.OK.value(),
                    "Admin user created. Please activate account",
                    adminService.registerAdmin(adminRequestDTO).toActivationTokenResponseDTO()
                )
            )
        }.getOrElse {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTO(HttpStatus.BAD_REQUEST.value(), "${it.message}"))
        }
    }

    @PostMapping("/v1/activate-account")
    fun activate(@Validated @RequestBody activationTokenRequestDTO: ActivationTokenRequestDTO): ResponseEntity<ResponseDTO<AdminResponseDTO>>{
        return runCatching {
            ResponseEntity.status(HttpStatus.OK).body(
                ResponseDTO(
                    HttpStatus.OK.value(),
                    "Account activate",
                    adminService.activateUser(activationTokenRequestDTO).toAdminResponseDTO())
            )
        }.getOrElse {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO(HttpStatus.BAD_REQUEST.value(), "${it.message}"))
        }

    }
}
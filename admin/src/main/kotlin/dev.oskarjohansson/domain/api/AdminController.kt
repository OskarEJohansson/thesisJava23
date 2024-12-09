package dev.oskarjohansson.domain.api

import dev.oskarjohansson.config.AdminRequestDTO
import dev.oskarjohansson.model.dto.LoginRequestDTO
import dev.oskarjohansson.model.dto.ResponseDTO
import dev.oskarjohansson.model.User
import dev.oskarjohansson.service.UserService
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/admin")
class AdminController(private val adminService: UserService, private val authenticationManager: AuthenticationManager) {

    @GetMapping("/v1/users")
    fun users(): ResponseEntity<ResponseDTO<List<User>>> {

      return  try {
            ResponseEntity.status(HttpStatus.OK).body(ResponseDTO(HttpStatus.OK.value(), "Users"))
        }catch (exception: IllegalStateException) {

            ResponseEntity.internalServerError().body(ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error getting users, ${exception.message}"))
        }
    }

    @PostMapping("/v1/login")
    fun login(@RequestBody loginRequestDTO: LoginRequestDTO): ResponseEntity<ResponseDTO<String>>{
        return runCatching {
            ResponseEntity.status(HttpStatus.OK).body(ResponseDTO(HttpStatus.OK.value(), "Login successful", runBlocking { adminService.loginAdmin(loginRequestDTO) } ))
        }.getOrElse {
            ResponseEntity.badRequest().body(ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Could not login user: ${it.message}"))
        }
    }

    @PostMapping("/v1/create-admin")
    fun create(@RequestBody adminRequestDTO: AdminRequestDTO): ResponseEntity<ResponseDTO<AdminResponseDTO>>{
        return runCatching {
            ResponseEntity.status(HttpStatus.OK).body(ResponseDTO(HttpStatus.OK.value(), "Admin user created"), adminService.)
        }.getOrElse {

        }
    }
}
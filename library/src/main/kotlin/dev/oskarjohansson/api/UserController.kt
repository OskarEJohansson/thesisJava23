package dev.oskarjohansson.api

import dev.oskarjohansson.api.dto.UserDTO
import dev.oskarjohansson.model.LoginRequestDTO
import dev.oskarjohansson.service.ApiService
import dev.oskarjohansson.service.UserService

import jakarta.validation.Valid
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(private val userService: UserService, private val apiService: ApiService) {

    @PostMapping("/v1/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequestDTO): ResponseEntity<String> {
        return runCatching {
            ResponseEntity.status(HttpStatus.OK).body(runBlocking { userService.loginUser(loginRequest) } )
        }.getOrElse { throw IllegalArgumentException("Could not login user: ${it.message}") }
    }

    @PostMapping("/v1/register-user")
    fun registerUser(@Valid @RequestBody registerUser: UserDTO): ResponseEntity<String> {

        return runCatching {
            userService.registerUser(registerUser)
             ResponseEntity.ok("User registered successfully")
        }.getOrElse {
            ResponseEntity.badRequest().body("Could not register user, ${it.message}")
        }
    }


//    @PutMapping
//    fun updateUser() {
//        TODO("Add logic for updating a user")
//    }
//
//    @DeleteMapping
//    fun deleteUser() {
//        TODO("Add logic for deleting a user")
//    }

}
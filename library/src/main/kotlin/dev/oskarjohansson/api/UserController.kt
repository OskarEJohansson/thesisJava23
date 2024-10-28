package dev.oskarjohansson.api

import dev.oskarjohansson.api.dto.UserDTO
import dev.oskarjohansson.model.LoginRequestDTO
import dev.oskarjohansson.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class UserController(private val userService: UserService) {


    @GetMapping
    suspend fun login(@Valid @RequestBody loginRequest: LoginRequestDTO):ResponseEntity<String> {

        return runCatching {
            ResponseEntity.status(HttpStatus.OK).body(userService.loginUser(loginRequest))
        }.getOrElse { throw IllegalArgumentException("Could not login user: ${it.message}") }
    }

    @PostMapping()
    fun registerUser(@Valid @RequestBody registerUser: UserDTO):ResponseEntity<String>{

        runCatching {
            userService.registerUser(registerUser)
            return ResponseEntity.ok("User registered successfully")
        }.getOrElse {
            throw IllegalArgumentException("Could not register user: ${it.message}")
        }
    }


    @PutMapping
    fun updateUser(){
        TODO("Add logic for updating a user")
    }

    @DeleteMapping
    fun deleteUser(){
        TODO("Add logic for deleting a user")
    }

}
package dev.oskarjohansson.api

import dev.oskarjohansson.api.dto.UserDTO
import dev.oskarjohansson.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException

@RestController
class UserController(private val userService: UserService) {


    @GetMapping
    fun login():ResponseEntity<String>{
        TODO("Add logic for logging in")
        return ResponseEntity.ok("")// TODO: add jwt-token ))
    }

    @PostMapping()
    fun registerUser(@Valid @RequestBody registerUser: UserDTO):ResponseEntity<String>{

        runCatching {
            userService.registerUser(registerUser)
            return ResponseEntity.ok("User registerd successfully")
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
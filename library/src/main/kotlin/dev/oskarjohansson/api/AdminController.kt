package dev.oskarjohansson.api

import dev.oskarjohansson.api.dto.ResponseDTO
import dev.oskarjohansson.domain.model.User
import dev.oskarjohansson.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/admin")
class AdminController(private val userService: UserService) {

    @GetMapping("/v1/users")
    fun users():ResponseEntity<ResponseDTO<List<User>>> {

      return  try {
            ResponseEntity.status(HttpStatus.OK).body(ResponseDTO(HttpStatus.OK.value(), "Users", userService.getUsers()))
        }catch (exception: IllegalStateException) {

            ResponseEntity.internalServerError().body(ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error getting users, ${exception.message}"))
        }
    }
}
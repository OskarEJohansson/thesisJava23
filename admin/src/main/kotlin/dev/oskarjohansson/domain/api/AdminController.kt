package dev.oskarjohansson.domain.api

import dev.oskarjohansson.api.dto.ResponseDTO
import dev.oskarjohansson.domain.service.AdminService
import dev.oskarjohansson.model.User
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/secured/v1")
class AdminController(private val adminService: AdminService) {

    private val LOG:Logger = LoggerFactory.getLogger(AdminController::class.java)

    @GetMapping("/users")
    fun users(httpServletRequest: HttpServletRequest): ResponseEntity<ResponseDTO<List<User>>> {

        LOG.info("Auth type: $httpServletRequest.authType")
        return try {
            ResponseEntity.status(HttpStatus.OK).body(
                ResponseDTO(
                    HttpStatus.OK.value(),
                    "Users",
                    adminService.getUsers())
            )
        } catch (exception: IllegalStateException) {
            ResponseEntity.internalServerError().body(
                ResponseDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error getting users, ${exception.message}"
                )
            )
        }
    }
}
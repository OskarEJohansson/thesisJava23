package dev.oskarjohansson.domain.api


import dev.oskarjohansson.domain.api.dto.request.AdminRequestDTO
import dev.oskarjohansson.domain.api.dto.response.AdminResponseDTO
import dev.oskarjohansson.model.User
import dev.oskarjohansson.model.dto.*
import dev.oskarjohansson.model.toActivationTokenResponseDTO
import dev.oskarjohansson.service.UserActivationService
import dev.oskarjohansson.service.UserService
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/admin")
class AdminController(private val adminService: UserService, private val userActivationService: UserActivationService) {

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
                    runBlocking { adminService.loginAdmin(loginRequestDTO) })
            )
        }.getOrElse {
            ResponseEntity.badRequest()
                .body(ResponseDTO(HttpStatus.BAD_REQUEST.value(),
                    "Could not login user: ${it.message}"))
        }
    }

    @PostMapping("/v1/create-admin")
    fun create(@Validated @RequestBody adminRequestDTO: AdminRequestDTO): ResponseEntity<ResponseDTO<String>> {
        return runCatching {

            adminService.registerAdmin(adminRequestDTO)

            ResponseEntity.status(HttpStatus.OK).body(
                ResponseDTO(
                    HttpStatus.OK.value(),
                    "Admin user created",
                    "Please activate account via link sent to your email address"
                )
            )
        }.getOrElse {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTO(HttpStatus.BAD_REQUEST.value(),
                    "${it.message}"))
        }
    }

    // TODO: FOR MANUAL TESTING PURPOSE
    @PostMapping("/v1/activate-account-TEST")
    fun activateTEST(@Validated @RequestBody activationTokenRequestDTOTEST: ActivationTokenRequestDTOTEST): ResponseEntity<ResponseDTO<AdminResponseDTO>> {
        return runCatching {
            ResponseEntity.status(HttpStatus.OK).body(
                ResponseDTO(
                    HttpStatus.OK.value(),
                    "Account activate",
                    userActivationService.activateUserTEST(activationTokenRequestDTOTEST).toAdminResponseDTO()
                )
            )
        }.getOrElse {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTO(HttpStatus.BAD_REQUEST.value(),
                    "${it.message}"))
        }

    }

//    // TODO: implement email delivery
//    @PostMapping("/v1/send-new-activation-token")
//    fun sendNewActivationToken(@Validated @RequestBody email: NewActivationTokenRequestDTO): ResponseEntity<ResponseDTO<ActivationTokenResponseDTO>> {
//        return runCatching {
//            ResponseEntity.status(HttpStatus.OK).body(
//                ResponseDTO(
//                    HttpStatus.OK.value(),
//                    "New activation token",
//                    userActivationService.newActivationToken(email).toActivationTokenResponseDTO()
//                )
//            )
//        }.getOrElse {
//            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//                ResponseDTO(
//                    HttpStatus.BAD_REQUEST.value(),
//                    "${it.message}"
//                )
//            )
//        }
//    }

    @PostMapping("/v1/activate-account/{activationToken}")
    fun activateAccount(@PathVariable activationToken:ActivationTokenRequestDto): ResponseEntity<ResponseDTO<AdminResponseDTO>>{

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
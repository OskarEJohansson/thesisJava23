package dev.oskarjohansson.exceptions

import dev.oskarjohansson.model.ErrorMessageModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.oauth2.jwt.JwtDecoderInitializationException
import org.springframework.validation.method.MethodValidationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val LOG: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler
    fun handleValidationException(exception: MethodValidationException): ResponseEntity<ErrorMessageModel> {

        val errorMessage = ErrorMessageModel(
            HttpStatus.BAD_REQUEST.value(),
            exception.message
        )

        LOG.info("ValidationException: ${exception.message}")
        LOG.debug("Validation Exception Stacktrace: ${exception.stackTraceToString()}")

        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }


    @ExceptionHandler
    fun handleUsernameNotFoundException(exception: UsernameNotFoundException): ResponseEntity<ErrorMessageModel>{

        val errorMessage= ErrorMessageModel(
            HttpStatus.BAD_REQUEST.value(),
            exception.message
        )

        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleIllegalStateException(exception: IllegalStateException): ResponseEntity<ErrorMessageModel>{

        val errorMessage = ErrorMessageModel(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            exception.message
        )

        return ResponseEntity(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler
    fun handleIllegalArgumentException(exception: IllegalArgumentException): ResponseEntity<ErrorMessageModel>{

        val errorMessage= ErrorMessageModel(
            HttpStatus.BAD_REQUEST.value(),
            exception.message
        )

        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleJwtDecoderInitializationException(exception: JwtDecoderInitializationException): ResponseEntity<ErrorMessageModel>{

        val errorMessage = ErrorMessageModel(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            exception.message
        )

        return ResponseEntity(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
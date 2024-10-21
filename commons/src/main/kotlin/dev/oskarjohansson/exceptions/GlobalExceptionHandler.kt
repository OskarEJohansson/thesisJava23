package dev.oskarjohansson.exceptions

import dev.oskarjohansson.exceptions.model.ErrorMessageModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.validation.method.MethodValidationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

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

        val errorMessageModel=ErrorMessageModel(
            HttpStatus.BAD_REQUEST.value(),
            exception.message
        )

        return ResponseEntity(errorMessageModel, HttpStatus.BAD_REQUEST)
    }
}
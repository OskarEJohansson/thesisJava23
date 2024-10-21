package dev.oskarjohansson.exceptions.model

import org.springframework.http.HttpStatus

data class ErrorMessageModel(private var status:Int? = null, private var message: String? = null )

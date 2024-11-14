package dev.oskarjohansson.api.dto

import org.springframework.validation.annotation.Validated

@Validated
data class BookRequestDTO(
    val bookId:String? = null,
    val title:String? = null
)

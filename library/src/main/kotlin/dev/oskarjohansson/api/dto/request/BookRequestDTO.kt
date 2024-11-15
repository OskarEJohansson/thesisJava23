package dev.oskarjohansson.api.dto.request

import org.springframework.validation.annotation.Validated

@Validated
data class BookRequestDTO(
    val bookId:String? = null,
    val title:String? = null
)

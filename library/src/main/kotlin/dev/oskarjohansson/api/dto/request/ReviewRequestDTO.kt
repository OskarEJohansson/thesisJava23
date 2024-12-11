package dev.oskarjohansson.api.dto.request

import org.springframework.validation.annotation.Validated

@Validated
data class ReviewRequestDTO(
    val text:String? = null,
    val rating:Int? = null,
    val bookId: String? = null,
    val reviewId: String? = null
)



package dev.oskarjohansson.api.dto

import jakarta.validation.constraints.Max
import org.springframework.validation.annotation.Validated

@Validated
data class ReviewRequestDTO(
    @Max(value = 255, message = "Max number of characters for a review text is 255.")
    val text:String? = null,
    val rating:Int? = null,
    val bookId: String? = null,
    val reviewId: String? = null)

// TODO: ADD validation for maximum number of characters for text and 1-5 range for rating


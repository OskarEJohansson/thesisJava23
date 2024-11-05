package dev.oskarjohansson.api.dto

import org.springframework.validation.annotation.Validated

@Validated
data class ReviewDTO(
    val review:String,
    val rating:Int,
    val bookId: String)

// TODO: ADD validation for maximum number of characters for text and 1-5 range for rating


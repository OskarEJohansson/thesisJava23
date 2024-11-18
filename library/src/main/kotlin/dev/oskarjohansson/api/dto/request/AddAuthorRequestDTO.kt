package dev.oskarjohansson.api.dto.request

import org.springframework.validation.annotation.Validated

@Validated
data class AddAuthorRequestDTO(
    val bookId:String,
    val authorName:String ? = null,
    val authorId:String? = null)

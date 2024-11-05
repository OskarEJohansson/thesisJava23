package dev.oskarjohansson.api.dto

import dev.oskarjohansson.domain.enums.Genres
import org.springframework.validation.annotation.Validated

@Validated
data class BookDTO(
    val title: String,
    val authorName: String,
    val genre: Genres,
    val isbn:String? = null)


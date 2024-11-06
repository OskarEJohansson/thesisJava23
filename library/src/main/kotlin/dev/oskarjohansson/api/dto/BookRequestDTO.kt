package dev.oskarjohansson.api.dto

import dev.oskarjohansson.domain.enums.Genres
import org.springframework.validation.annotation.Validated

@Validated
data class BookRequestDTO(
    val title: String,
    val authorName: String,
    val genre: Genres,
)


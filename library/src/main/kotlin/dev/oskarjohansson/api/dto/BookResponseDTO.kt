package dev.oskarjohansson.api.dto

import dev.oskarjohansson.domain.enums.Genres

data class BookResponseDTO(
    val bookId: String,
    val title: String,
    val authors: List<AuthorInBookResponseDTO>? = null,
    val genres: Genres,
)

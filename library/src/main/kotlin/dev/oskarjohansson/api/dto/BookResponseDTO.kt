package dev.oskarjohansson.api.dto

import dev.oskarjohansson.domain.enums.Genres

class BookResponseDTO(
    val bookId: String,
    val title: String,
    val authors: List<AuthorResponseDTO>,
    val genres: Genres,
)

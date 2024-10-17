package dev.oskarjohansson.domain.model

data class Book(
    val bookid: String,
    val title: String,
    val authorIds: List<String>,
    val reviewIds: List<String>,
    val genres: Genres,
    val isbn: String
)
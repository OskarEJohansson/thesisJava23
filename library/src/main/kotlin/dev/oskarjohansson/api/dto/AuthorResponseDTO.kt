package dev.oskarjohansson.api.dto

data class AuthorResponseDTO(
    val authorID: String,
    val authorName: String,
    val publishedBooks: List<BookInAuthorResponseDTO>

)

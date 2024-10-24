package dev.oskarjohansson.domain.model

import dev.oskarjohansson.domain.enums.Genres
import org.springframework.data.mongodb.core.mapping.Document

@Document(collation = "Books")
data class Book(
    val bookId: String,
    val title: String,
    val authorIds: List<String>,
    val reviewIds: List<String>,
    val genres: Genres,
    val isbn: String
)
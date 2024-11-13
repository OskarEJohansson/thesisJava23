package dev.oskarjohansson.domain.model

import dev.oskarjohansson.domain.enums.Genres
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId


@Document(collection = "Books")
data class Book(
    @MongoId
    val bookId: String? = null,
    val title: String,
    val authorIds: List<String>,
    val genres: Genres,
)
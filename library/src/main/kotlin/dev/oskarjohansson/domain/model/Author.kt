package dev.oskarjohansson.domain.model

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId

@Document(collection = "Authors")
data class Author(
    @MongoId
    val authorId: String?,
    val authorName: String,
    // TODO: PublishedBooks contain reviewIds. Find better suiting name for the variable.
    val publishedBooks: List<String>?
) {
}
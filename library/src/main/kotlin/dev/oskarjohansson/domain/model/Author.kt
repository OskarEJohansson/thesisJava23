package dev.oskarjohansson.domain.model

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId

@Document(collection = "Authors")
data class Author(
    @MongoId
    val authorId: String? = null,
    val authorName: String
)
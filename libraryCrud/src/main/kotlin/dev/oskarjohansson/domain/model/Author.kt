package dev.oskarjohansson.domain.model

import org.springframework.data.mongodb.core.mapping.Document

@Document(collation = "Authors")
data class Author(val authorId:String,
                  val authorName: String,
                    // TODO: PublishedBooks contain reviewIds. Find better suiting name for the variable.
                  val publishedBooks: List<String>) {
}
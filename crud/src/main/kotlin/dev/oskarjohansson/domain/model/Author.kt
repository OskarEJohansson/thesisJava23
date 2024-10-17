package dev.oskarjohansson.domain.model

data class Author(val authorId:String,
                  val authorName: String,
                    // TODO: PublishedBooks contain reviewIds. Find better suiting name for the variable.
                  val publishedBooks: List<String>) {
}
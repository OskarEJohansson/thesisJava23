package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.*
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.domain.model.Book
import dev.oskarjohansson.domain.model.Review
import java.lang.IllegalArgumentException

import java.time.LocalDateTime

fun ReviewRequestDTO.toReview(userId: String): Review = Review(
    null,
    this.text ?: throw IllegalArgumentException("Review text must not be null"),
    this.rating ?: throw IllegalArgumentException("Review rating must not be null"),
    LocalDateTime.now(),
    null,
    userId,
    this.bookId ?: throw IllegalArgumentException("BookId in Review must nut be null")
)

fun Book.toBookResponseDTO(authors: List<AuthorInBookResponseDTO>): BookResponseDTO =
    BookResponseDTO(
        bookId = this.bookId!!,
        title = this.title,
        authors = authors,
        genres = this.genres
    )

fun Author.toAuthorResponseDTO(books: List<BookInAuthorResponseDTO>): AuthorResponseDTO =
    AuthorResponseDTO(
        this.authorId!!,
        this.authorName,
        books
    )

fun Review.toReviewResponseDTO(): ReviewResponseDTO = ReviewResponseDTO(
    text = this.text,
    rating = this.rating,
    userId = this.userId

)


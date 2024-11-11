package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.*
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.domain.model.Book
import dev.oskarjohansson.domain.model.Review

import java.time.LocalDateTime

// TODO: Create into an extension function of ReviewDTO
fun createReviewFromReviewDto(review: ReviewDTO, userId: String): Review = Review(
    text = review.review,
    rating = review.rating,
    bookId = review.bookId,
    createdAt = LocalDateTime.now(),
    userId = userId,
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


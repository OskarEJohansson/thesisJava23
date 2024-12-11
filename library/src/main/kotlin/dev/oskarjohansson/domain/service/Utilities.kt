package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.request.RegisterBookRequestDTO
import dev.oskarjohansson.api.dto.request.ReviewRequestDTO
import dev.oskarjohansson.api.dto.response.*
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.domain.model.Book
import dev.oskarjohansson.domain.model.Review
import java.lang.IllegalArgumentException

import java.time.LocalDateTime

fun ReviewRequestDTO.toReviewWithReviewIdNull(userId: String): Review = Review(
    null,
    this.text ?: throw IllegalArgumentException("Review text must not be null"),
    this.rating ?: throw IllegalArgumentException("Review rating must not be null"),
    LocalDateTime.now(),
    null,
    userId,
    this.bookId ?: throw IllegalArgumentException("BookId in Review must nut be null")
)

fun Review.toUpdatedReview(reviewRequest: ReviewRequestDTO) =
    Review(
        this.reviewId,
        reviewRequest.text ?:this.text,
        reviewRequest.rating ?: this.rating,
        this.createdAt,
        LocalDateTime.now(),
        this.userId,
        this.bookId
    )

fun Review.toReviewResponseDTO(): ReviewResponseDTO = ReviewResponseDTO(
    text = this.text,
    rating = this.rating,
    userId = this.userId,
    reviewId = this.reviewId!! //ReviewId is created when Review is persisted
)

fun Book.toBookResponseDTO(authors: List<AuthorInBookResponseDTO>): BookResponseDTO =
    BookResponseDTO(
        bookId = this.bookId!!, //BookId is created when Book is persisted
        title = this.title,
        authors = authors,
        genres = this.genres
    )

fun Author.toAuthorResponseDTO(books: List<BookInAuthorResponseDTO>): AuthorResponseDTO =
    AuthorResponseDTO(
        this.authorId!!, //AuthorId is created when Author is persisted
        this.authorName,
        publishedBooks = books
    )

fun RegisterBookRequestDTO.toBook(authorIds: List<String>) = Book(
    null,
    title = this.title,
    authorIds = authorIds,
    genres = this.genre
)


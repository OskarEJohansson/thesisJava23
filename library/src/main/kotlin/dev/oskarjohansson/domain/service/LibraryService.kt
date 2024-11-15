package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.*
import dev.oskarjohansson.domain.model.Book
import dev.oskarjohansson.domain.model.Review
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class LibraryService(

    private val authorService: AuthorService,
    private val bookService: BookService,
    private val reviewService: ReviewService
) {

    // TODO: Rename to something more describing or split into multiple methods?
    // TODO: Write test
    fun saveBook(bookRequest: RegisterBookRequestDTO): BookResponseDTO {
        return authorService.getOrCreateAuthors(bookRequest.authors)
            .map { author -> author.authorId!! }
            .let { authorIds ->
                bookService.saveBook(bookRequest, authorIds).toBookResponseDTO(
                    authorService.createAuthorResponseDTO(authorIds)
                )
            }
    }

    fun saveAuthor(authorName: String): AuthorResponseDTO {
        return authorService.saveAuthor(authorName).toAuthorResponseDTO(emptyList())
    }

    fun saveReview(review: ReviewRequestDTO, jwt: Jwt): Review {
        val book = bookService.findBookById(review.bookId!!) // null check in controller
        val userId = jwt.claims["userId"].toString()

        val existingReview = book.bookId?.let { reviewService.findByBookIdAndUserId(it, userId) }
            ?: return reviewService.createReview(review, userId)

        throw IllegalArgumentException("Review already exist for user, reviewId: ${existingReview.reviewId}")
    }

    fun getBookByIdOrTitle(bookRequestDTO: BookRequestDTO): BookResponseDTO {
        return bookService.findBookByIdOrTitle(bookRequestDTO).let { book ->
            book.toBookResponseDTO(
                authorService.createAuthorResponseDTO(book.authorIds)
            )
        }
    }

    fun getBookById(bookId: String): Book =
        bookService.findBookById(bookId)

    fun getBooks(pageable: Pageable): Page<BookResponseDTO> {
        return bookService.findAllBooksPageable(pageable).map { book ->
            book.toBookResponseDTO(
                authorService.createAuthorResponseDTO(book.authorIds)
            )
        }
    }

    fun getAuthors(pageable: Pageable): Page<AuthorResponseDTO> {
        return authorService.getAuthors(pageable).map { author ->
            bookService.createBookInAuthorResponseDTO(author.authorId!!) // author must have id in db
                ?.let {
                    author.toAuthorResponseDTO(it)
                }
        }
    }

    fun getReviews(pageable: Pageable, bookId: String): Page<ReviewResponseDTO> {
        return bookService.findBookById(bookId).let { book ->
            reviewService.createPageableReviews(pageable, book.bookId!!)
        }
    }

    fun deleteReview(jwt: Jwt, reviewId: String) {
        val review = reviewService.findById(reviewId)
        return run {
            review.reviewId?.takeIf {
                jwt.claims["userId"].toString() == review.userId
            }?.let { reviewService.deleteById(it) }
        }
    }

    fun updateReview(jwt: Jwt, reviewRequest: ReviewRequestDTO): ReviewResponseDTO {
        val review = reviewService.findById(reviewRequest.reviewId!!)
        val isUser = jwt.claims["userId"].toString() == review.userId
        val updatedReview = review.takeIf { isUser }?.toUpdatedReview(reviewRequest)

        val response =
            updatedReview?.let { reviewService.save(it) }
                ?: throw IllegalArgumentException("UserId and userId on review did not match")

        return response.toReviewResponseDTO()

    }
}
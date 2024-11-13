package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.*
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.domain.model.Book
import dev.oskarjohansson.domain.model.Review
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.time.LocalDateTime

@Service
class LibraryService(

    private val authorService: AuthorService,
    private val bookService: BookService,
    private val reviewService: ReviewService
) {

    fun saveBook(book: BookRequestDTO): BookResponseDTO {
        val authorId = authorService.getOrCreateAuthor(book.authorName)
        val savedBook = bookService.saveBook(book, authorId)
        val authors = authorService.createAuthorResponseDTO(savedBook.authorIds)
        return savedBook.toBookResponseDTO(authors)
    }

    fun saveAuthor(authorName: String): Author {
        return authorService.saveAuthor(authorName)
    }

    fun createReview(review: ReviewRequestDTO, jwt: Jwt): Review {

        val book = bookService.findBookById(review.bookId!!) // null check in controller
        val userId = jwt.claims["userId"].toString()
        val existingReview = book.bookId?.let { reviewService.findByBookIdAndUserId(it, userId) }

        return review.takeIf { existingReview == null }?.let {
            reviewService.createReview(review, userId)
        } ?: throw IllegalArgumentException("Review already exist for user, ${existingReview?.reviewId}")
    }


    fun getBook(bookId: String): Book =
        bookService.findBookById(bookId)


    fun getBooks(pageable: Pageable): Page<BookResponseDTO> {
        return runCatching {
            bookService.findAllBooksPageable(pageable).map { book ->
                book.toBookResponseDTO(
                    authorService.createAuthorResponseDTO(book.authorIds)
                )
            }
        }.getOrElse { throw IllegalStateException("Error while loading books: ${it.message}") }
    }


    fun getAuthors(pageable: Pageable): Page<AuthorResponseDTO> {
        val authors = authorService.getAuthors(pageable)

        return authors.map { author ->
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
        // TODO: Check that reviewId exist
        // TODO: Check that userID in jwt exist on Review
        // TODO: Delete review

        val review = reviewService.findById(reviewId)
            ?: throw IllegalArgumentException("No review found")

        val isUser = jwt.claims["userId"].toString() == review.userId

        return run {
            review.reviewId?.takeIf {
                isUser
            }?.let { reviewService.deleteById(it) }
        }

    }

    fun updateReview(jwt: Jwt, reviewRequest: ReviewRequestDTO): ReviewResponseDTO {
        // TODO: Check that review exist
        // TODO: Check that user exist on review
        // TODO: Create Review()
        // TODO: Check if text is null, if not add to a ReviewObject
        // TODO: check if rating is null, if not add to ReviewObject


        val review = reviewService.findById(reviewRequest.reviewId!!)
            ?: throw IllegalArgumentException("No review found")

        val isUser = jwt.claims["userId"].toString() == review.userId

        val updatedReview = review.takeIf { isUser }
            ?.let {
                Review(
                    review.reviewId,
                    reviewRequest.text ?: review.text,
                    reviewRequest.rating ?: review.rating,
                    review.createdAt,
                    LocalDateTime.now(),
                    review.userId,
                    review.bookId
                )
            }

        val response =
            updatedReview?.let { reviewService.updateReview(it) }
                ?: throw IllegalStateException("Could not update review")

        return response.toReviewResponseDTO()

    }
}
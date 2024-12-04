package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.request.RegisterBookRequestDTO
import dev.oskarjohansson.api.dto.request.ReviewRequestDTO
import dev.oskarjohansson.api.dto.response.AuthorInBookResponseDTO
import dev.oskarjohansson.api.dto.response.BookInAuthorResponseDTO
import dev.oskarjohansson.api.dto.response.ReviewResponseDTO
import dev.oskarjohansson.domain.enums.Genres
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.domain.model.Book
import dev.oskarjohansson.domain.model.Review
import io.ktor.util.reflect.*
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import kotlin.test.*

class LibraryUtilitiesKtTest {

    @Test
    fun `Test that toReviewWithReviewIdNull converts ReviewRequestDTO correctly with valid inputs`() {

        val reviewRequest = ReviewRequestDTO("Text", 5, "BookId", "ReviewId")
        val review = reviewRequest.toReviewWithReviewIdNull("UserId")

        assertEquals("Text", review.text)
        assertEquals(5, review.rating)
        assertEquals("UserId", review.userId)
        assertNotNull(review.createdAt)
        assertNull(review.updatedAt, review.reviewId)
    }

    @Test
    fun `Test that toReviewWithReviewIdNull throws error when text is missing`() {
        val reviewRequest = ReviewRequestDTO(null, 5, "BookId", "ReviewId")

        assertThrows<IllegalArgumentException> { reviewRequest.toReviewWithReviewIdNull("UserId") }
    }

    @Test
    fun `Test that toReviewWithReviewIdNull throws error when rating is missing`() {
        val reviewRequest = ReviewRequestDTO("Text", null, "BookId", "ReviewId")

        assertThrows<IllegalArgumentException> { reviewRequest.toReviewWithReviewIdNull("UserId") }
    }

    @Test
    fun `Test that toReviewWithReviewIdNull throws error when bookId is missing`() {
        val reviewRequest = ReviewRequestDTO("Text", 5, null, "ReviewId")

        assertThrows<IllegalArgumentException> { reviewRequest.toReviewWithReviewIdNull("UserId") }
    }

    @Test
    fun `Test that toUpdatedReview converts correctly with valid inputs`() {
        val time = LocalDateTime.now()
        val review = Review("ReviewId", "Persisted Text", 1, time, null, "UserId", "BookId")
        val reviewRequest = ReviewRequestDTO("Updated Review Text", 5)
        val updatedReview = review.toUpdatedReview(reviewRequest)

        assertEquals("ReviewId", updatedReview.reviewId)
        assertEquals("Updated Review Text", updatedReview.text)
        assertEquals(5, updatedReview.rating)
        assertEquals(time, updatedReview.createdAt)
        assertNotNull(updatedReview.updatedAt)
        assertDoesNotThrow { review.toUpdatedReview(reviewRequest) }
    }

    @Test
    fun `Test that toUpdatedReview updates text and not rating when rating is null in request`() {
        val time = LocalDateTime.now()
        val review = Review("ReviewId", "Persisted Text", 1, time, null, "UserId", "BookId")
        val reviewRequest = ReviewRequestDTO("Updated Review Text", null)
        val updatedReview = review.toUpdatedReview(reviewRequest)

        assertEquals("ReviewId", updatedReview.reviewId)
        assertEquals("Updated Review Text", updatedReview.text)
        assertEquals(1, updatedReview.rating)
        assertEquals(time, updatedReview.createdAt)
        assertNotNull(updatedReview.updatedAt)
        assertDoesNotThrow { review.toUpdatedReview(reviewRequest) }
    }

    @Test
    fun `Test that toUpdatedReview updates rating and not text when text is null in request`() {
        val time = LocalDateTime.now()
        val review = Review("ReviewId", "Persisted Text", 1, time, null, "UserId", "BookId")
        val reviewRequest = ReviewRequestDTO(null, 5)
        val updatedReview = review.toUpdatedReview(reviewRequest)

        assertEquals("ReviewId", updatedReview.reviewId)
        assertEquals("Persisted Text", updatedReview.text)
        assertEquals(5, updatedReview.rating)
        assertEquals(time, updatedReview.createdAt)
        assertNotNull(updatedReview.updatedAt)
        assertDoesNotThrow { review.toUpdatedReview(reviewRequest) }
    }


    @Test
    fun `Test that toReviewResponseDTO converts correctly`() {
        val time = LocalDateTime.now()
        val review = Review("ReviewId", "Persisted Text", 1, time, null, "UserId", "BookId")

        val reviewResponseDto = review.toReviewResponseDTO()

        assertDoesNotThrow { review.toReviewResponseDTO() }
        assertEquals("Persisted Text", reviewResponseDto.text)
        assertEquals(1, reviewResponseDto.rating)
        assertEquals("UserId", reviewResponseDto.userId)
        assertEquals("ReviewId", reviewResponseDto.reviewId)
        assertTrue(reviewResponseDto.instanceOf(ReviewResponseDTO::class))

    }

    @Test
    fun `Test that toBookResponseDTO converts Book correctly with one author`() {
        val book = Book("BookId", "Book Title", listOf("Author 1"), Genres.FANTASY)
        val bookResponse = book.toBookResponseDTO(listOf(AuthorInBookResponseDTO("AuthorId", "Author 1")))

        assertEquals("BookId", bookResponse.bookId)
        assertEquals("Book Title", bookResponse.title)
        assertEquals(Genres.FANTASY, bookResponse.genres)
        assertTrue(bookResponse.authors!![0].instanceOf(AuthorInBookResponseDTO::class))
        assertEquals(1, bookResponse.authors!!.size)

    }

    @Test
    fun `Test that toBookResponseDTO converts Book correctly with multiple authors`() {
        val book = Book("BookId", "Book Title", listOf("Author 1"), Genres.FANTASY)
        val bookResponse = book.toBookResponseDTO(listOf(
            AuthorInBookResponseDTO("AuthorId", "Author 1"),
            AuthorInBookResponseDTO("AuthorId", "Author 2")
        ))

        assertEquals("BookId", bookResponse.bookId)
        assertEquals("Book Title", bookResponse.title)
        assertEquals(Genres.FANTASY, bookResponse.genres)
        assertTrue(bookResponse.authors!![1].instanceOf(AuthorInBookResponseDTO::class))
        assertEquals(2, bookResponse.authors!!.size)
    }

    @Test
    fun `Test that toAuthorResponseDTO converts Author correctly with one book`() {

        val author = Author("AuthorId", "Author1")
        val bookInAuthor = BookInAuthorResponseDTO("BookId", "Book1")
        val authorResponse = author.toAuthorResponseDTO(listOf(bookInAuthor))

        assertEquals("AuthorId", authorResponse.authorID)
        assertEquals("Author1", authorResponse.authorName)
        assertTrue { authorResponse.publishedBooks[0].instanceOf(BookInAuthorResponseDTO::class) }
        assertEquals(1, authorResponse.publishedBooks.size)
    }

    @Test
    fun `Test that toAuthorResponseDTO converts Author correctly with many books`() {

        val author = Author("AuthorId", "Author1")
        val authorResponse = author.toAuthorResponseDTO(listOf(
            BookInAuthorResponseDTO("BookId", "Book1"),
            BookInAuthorResponseDTO("BookId", "Book2")
        ))

        assertEquals("AuthorId", authorResponse.authorID)
        assertEquals("Author1", authorResponse.authorName)
        assertTrue { authorResponse.publishedBooks[1].instanceOf(BookInAuthorResponseDTO::class) }
        assertEquals(2, authorResponse.publishedBooks.size)
    }

    @Test
    fun `Test that toBook converts RegisterBookRequestDTO correctly with one author`() {

        val request = RegisterBookRequestDTO("Book1", listOf("authorName"), Genres.FANTASY)
        val book = request.toBook(listOf("AuthorId132"))
        assertEquals("Book1", book.title)
        assertNull(book.bookId)
        assertEquals(Genres.FANTASY, book.genres)
        assertEquals(1, book.authorIds.size)
        assertTrue { book.authorIds[0].instanceOf(String::class) }


    }

    @Test
    fun `Test that toBook converts RegisterBookRequestDTO correctly with many authors`() {

        val request = RegisterBookRequestDTO("Book1", listOf("authorName", "AnotherAuthor"), Genres.FANTASY)
        val book = request.toBook(listOf("AuthorId132", "AuthorIdABC"))
        assertEquals("Book1", book.title)
        assertNull(book.bookId)
        assertEquals(Genres.FANTASY, book.genres)
        assertEquals(2, book.authorIds.size)
        assertTrue { book.authorIds[1].instanceOf(String::class) }


    }


}
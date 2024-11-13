package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.ReviewRequestDTO
import dev.oskarjohansson.domain.enums.Genres
import dev.oskarjohansson.domain.model.Book
import dev.oskarjohansson.domain.model.Review
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.security.oauth2.jwt.Jwt
import java.time.LocalDateTime


class LibraryServiceTest {

    private val authorService = mockk<AuthorService>()
    private val bookService = mockk<BookService>()
    private val reviewService = mockk<ReviewService>()
    private val libraryService = LibraryService(authorService, bookService, reviewService)
    private val jwt = mockk<Jwt>()
    private val book = Book("1234", "Book1", listOf("1234", "1235"), Genres.FANTASY)
    private val review = Review("12345", "Review 123", 5, LocalDateTime.now(), null, "user123", "1234")

    private val reviewRequestDto = ReviewRequestDTO("Review 123", 5, "1234")


    @Test
    fun `assertDoesNotThrow that createReview does not throw exception with valid inputs`() {

        every { jwt.claims } returns mapOf("userId" to "user123")
        every { bookService.findBookById(any()) } returns book
        every { reviewService.findByBookIdAndUserId(any(), any()) } returns null
        every { reviewService.createReview(any(), any()) } returns review
        assertDoesNotThrow { libraryService.createReview(reviewRequestDto, jwt) }
    }

    @Test
    fun `test that createReview throws exception when user has an existing review`() {

        every { jwt.claims } returns mapOf("userId" to "user123")
        every { bookService.findBookById(any()) } returns book
        every { reviewService.findByBookIdAndUserId(any(), any()) } returns review
        assertThrows<IllegalArgumentException> { libraryService.createReview(reviewRequestDto, jwt) }
    }


    @Test
    fun `test that createReview throws IllegalArgumentException when user already has registered a review`() {

        every { bookService.findBookById(any()) } returns book
        every { reviewService.findByBookIdAndUserId(any(), any()) } returns review
        every { jwt.claims } returns mapOf("userId" to "user123")
        assertThrows<IllegalArgumentException> { libraryService.createReview(reviewRequestDto, jwt) }
    }

    @Test
    fun `test that createReview does not throw IllegalArgumentException when user has no review registered`() {

        every { bookService.findBookById(any()) } returns book
        every { reviewService.findByBookIdAndUserId(any(), any()) } returns null
        every { jwt.claims } returns mapOf("userId" to "user123")
        every { reviewService.createReview(any(), any()) } returns review
        assertDoesNotThrow{ libraryService.createReview(reviewRequestDto, jwt) }
    }

    @Test
    fun `test that createReview throws IllegalArgumentException when Book is not found`() {

        every { bookService.findBookById(any()) } throws IllegalStateException()
        every { reviewService.findByBookIdAndUserId(any(), any()) } returns null
        every { jwt.claims } returns mapOf("userId" to "user123")
        every { reviewService.createReview(any(), any()) } returns review
        assertThrows<IllegalStateException>{ libraryService.createReview(reviewRequestDto, jwt) }
    }

    @Test
    fun `test that createReview throws IllegalArgumentException review can't be persisted`() {
        every { bookService.findBookById(any()) } returns book
        every { reviewService.findByBookIdAndUserId(any(), any()) } returns null
        every { jwt.claims } returns mapOf("userId" to "user123")
        every { reviewService.createReview(any(), any()) } throws IllegalStateException()
        assertThrows<IllegalStateException>{ libraryService.createReview(reviewRequestDto, jwt) }
    }

}
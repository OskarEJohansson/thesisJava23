package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.ReviewDTO
import dev.oskarjohansson.respository.ReviewRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.security.oauth2.jwt.Jwt

import kotlin.IllegalStateException

import kotlin.test.Test

class ReviewServiceTest {

    private val bookService = mockk<BookService>()
    private val reviewRepository = mockk<ReviewRepository>()
    private val jwt = mockk<Jwt>()
    private val reviewService = ReviewService(bookService, reviewRepository)
    private val reviewDto = ReviewDTO("Review 123", 5, "abcd")


    @Test
    fun `test that createReview does not throw exception with valid input`() {
        every { jwt.claims } returns mapOf("userId" to "user123")
        every { bookService.findBookById(reviewDto.bookId) } returns true
        every { reviewRepository.save(any())} returns mockk()
        assertDoesNotThrow { reviewService.createReview(reviewDto, jwt) }
    }

    @Test
    fun `test that createReview throws IllegalArgumentException when a invalid userId is posted`() {
        every { jwt.claims } returns emptyMap()
        every { bookService.findBookById(reviewDto.bookId) } returns true
        every { reviewRepository.save(any())} returns mockk()
        assertThrows<IllegalArgumentException>{ reviewService.createReview(reviewDto, jwt) }
    }

    @Test
    fun `test that createReview throws IllegalArgumentException when a invalid bookId is posted`() {
        every { jwt.claims["userId"] } returns mapOf("userId" to "user123")
        every { bookService.findBookById(reviewDto.bookId) } returns false
        every { reviewRepository.save(any())} returns mockk()
        assertThrows<IllegalArgumentException>{ reviewService.createReview(reviewDto, jwt) }
    }

    @Test
    fun `test that repository throws different error when entity cant be persisted`() {
        every { jwt.claims } returns mapOf("userId" to "user123")
        every { bookService.findBookById(reviewDto.bookId) } returns true
        every { reviewRepository.save(any())}.throws(IllegalStateException())
        assertThrows<IllegalStateException>{ reviewService.createReview(reviewDto, jwt) }

    }

}
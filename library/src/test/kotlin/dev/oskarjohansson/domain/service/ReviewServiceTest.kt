package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.ReviewDTO
import dev.oskarjohansson.respository.ReviewRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.assertThrows
import org.springframework.security.oauth2.jwt.Jwt

import kotlin.test.Test

class ReviewServiceTest {

    val bookService = mockk<BookService>()
    val reviewRepository = mockk<ReviewRepository>()
    val jwt = mockk<Jwt>()
    val reviewService = ReviewService(bookService, reviewRepository)
    val reviewDto = ReviewDTO("Review 123", 5, "abcd")
    val review = createReviewFromReviewDto()



    @Test
    fun `test that createReview is successful`() {
        every { jwt.claims } returns mapOf("userId" to "user123")
        every { bookService.findBookById(reviewDto.bookId) } returns true
        every { reviewRepository.save(any())} returns mockk()

    }

    @Test
    fun `test that createReview throws IllegalStateException when a invalid userId is posted`() {
        every { jwt.claims } returns emptyMap()
        every { bookService.findBookById(reviewDto.bookId) } returns true
        every { reviewRepository.save(any())} returns mockk()
        assertThrows<IllegalStateException> { reviewService.createReview(reviewDto, jwt)  }
    }

}
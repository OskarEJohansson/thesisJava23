package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.ReviewDTO
import dev.oskarjohansson.domain.model.Review
import dev.oskarjohansson.respository.ReviewRepository
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import kotlin.IllegalStateException


@Service
class ReviewService(private val bookService: BookService, private val reviewRepository: ReviewRepository) {

    fun createReview(review: ReviewDTO, jwt: Jwt): Review {
        return runCatching {

            jwt.claims["userId"]?.toString()
                ?.takeIf { bookService.findBookById(review.bookId) }
                ?.let { userId -> createReviewFromReviewDto(review, userId) }
                ?.let { review -> reviewRepository.save(review) }
                ?: throw IllegalStateException("Invalid userId or bookId ")
        }.getOrElse { throw IllegalStateException("Could not save review, ${it.message}") }
    }

}

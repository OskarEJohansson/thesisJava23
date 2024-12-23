package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.request.ReviewRequestDTO
import dev.oskarjohansson.api.dto.response.ReviewResponseDTO
import dev.oskarjohansson.domain.model.Review
import dev.oskarjohansson.respository.ReviewRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException


@Service
class ReviewService(private val reviewRepository: ReviewRepository) {

    fun createReview(reviewRequest: ReviewRequestDTO, userId: String): Review {
        return reviewRequest.toReviewWithReviewIdNull(userId)
            .let { review -> reviewRepository.save(review) }
    }

    fun findByBookIdAndUserId(bookId: String, userId: String): Review? {
        return reviewRepository.findByBookIdAndUserId(bookId, userId)
    }

    fun createPageableReviews(pageable: Pageable, bookId: String): Page<ReviewResponseDTO> {
        return reviewRepository.findByBookId(pageable, bookId)?.map { review ->
            review.toReviewResponseDTO()
        } ?: throw IllegalStateException("Could not find any reviews for book $bookId")
    }

    fun save(review: Review): Review = reviewRepository.save(review)

    fun findById(reviewId: String): Review = reviewRepository.findByReviewId(reviewId) ?: throw IllegalArgumentException("Could not find Review with reviewId: $reviewId")


    fun deleteById(reviewId: String) {
        return reviewRepository.deleteById(reviewId)
    }
}

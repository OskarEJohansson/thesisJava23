package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.ReviewDTO
import dev.oskarjohansson.api.dto.ReviewResponseDTO
import dev.oskarjohansson.domain.model.Review
import dev.oskarjohansson.respository.ReviewRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class ReviewService(private val reviewRepository: ReviewRepository) {

    fun createReview(review: ReviewDTO, userId: String): Review {

        return run {
            createReviewFromReviewDto(review, userId)
                .let { reviewRepository.save(it) }
        }
    }

    fun findByBookIdAndUserId(bookId:String, userId: String): Review?{
        return reviewRepository.findByBookIdAndUserId(bookId, userId)
    }

    fun createPageableReviews(pageable: Pageable, bookId: String): Page<ReviewResponseDTO> {
        return reviewRepository.findByBookId(pageable, bookId)?.map { review ->
            review.toReviewResponseDTO()
        }
            ?: throw IllegalStateException("Could not find any reviews for book $bookId")
    }

    fun findById(reviewId: String): Review? {
        return reviewRepository.findByReviewId(reviewId)
    }

    fun deleteById(reviewId:String){
        return reviewRepository.deleteById(reviewId)
    }
}
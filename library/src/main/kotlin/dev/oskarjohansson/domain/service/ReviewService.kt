package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.ReviewDTO
import dev.oskarjohansson.domain.model.Review
import dev.oskarjohansson.respository.ReviewRepository
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException


@Service
class ReviewService(private val reviewRepository: ReviewRepository) {

    fun createReview(review: ReviewDTO, userId: String): Review {

        return run {
            createReviewFromReviewDto(review, userId)
                .let { reviewRepository.save(it) }
        }
    }

    fun findReviewCreatedByUser(userId: String): Boolean {

        return runCatching { reviewRepository.findByUserId(userId).isEmpty }.getOrElse {
            throw IllegalArgumentException(
                "Review exist for user with userId $userId",
            )
        }

    }
}

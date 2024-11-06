package dev.oskarjohansson.domain

import dev.oskarjohansson.api.dto.ReviewDTO
import dev.oskarjohansson.domain.model.Review
import java.time.LocalDateTime

fun createReviewFromReviewDto(review: ReviewDTO, userId: String): Review = Review(
    text = review.review,
    rating = review.rating,
    bookId = review.bookId,
    createdAt = LocalDateTime.now(),
    userId = userId,
)


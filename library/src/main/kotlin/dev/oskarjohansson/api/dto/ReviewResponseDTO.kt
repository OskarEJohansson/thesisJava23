package dev.oskarjohansson.api.dto

data class ReviewResponseDTO(
    val text: String,
    val rating: Int,
    val userId: String,
    val reviewId: String
) {
}
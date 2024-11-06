package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.AuthorResponseDTO
import dev.oskarjohansson.api.dto.BookResponseDTO
import dev.oskarjohansson.api.dto.ReviewDTO
import dev.oskarjohansson.domain.model.Book
import dev.oskarjohansson.domain.model.Review

import java.time.LocalDateTime

fun createReviewFromReviewDto(review: ReviewDTO, userId: String): Review = Review(
    text = review.review,
    rating = review.rating,
    bookId = review.bookId,
    createdAt = LocalDateTime.now(),
    userId = userId,
)

fun Book.toBookResponseDTO(authors:List<AuthorResponseDTO>) {
    this.bookId?. let {
        BookResponseDTO(
            bookId = this.bookId,
            title = this.title,
            authors = authors,
            genres = this.genres
        )
    }
}

fun createAuthorResonseDTOList(authorId:String){



}



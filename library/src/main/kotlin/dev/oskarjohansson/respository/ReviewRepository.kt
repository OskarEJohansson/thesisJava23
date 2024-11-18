package dev.oskarjohansson.respository

import dev.oskarjohansson.domain.model.Review
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ReviewRepository:MongoRepository<Review, String> {

    fun findByBookId(pageable: Pageable, bookId:String): Page<Review>?
    fun findByBookIdAndUserId(bookId: String, userId: String): Review?
    fun findByReviewId(reviewId:String): Review?

}
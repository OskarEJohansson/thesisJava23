package dev.oskarjohansson.respository

import dev.oskarjohansson.domain.model.Review
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ReviewRepository:MongoRepository<Review, String> {
}
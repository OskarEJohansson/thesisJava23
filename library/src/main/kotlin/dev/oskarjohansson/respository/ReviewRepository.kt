package dev.oskarjohansson.respository

import dev.oskarjohansson.domain.model.Review
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ReviewRepository:MongoRepository<Review, String> {

    fun findByUserId(userId:String): Optional<Review>
}
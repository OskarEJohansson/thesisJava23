package dev.oskarjohansson.respository

import dev.oskarjohansson.domain.model.Book
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository :MongoRepository<Book, String> {
}
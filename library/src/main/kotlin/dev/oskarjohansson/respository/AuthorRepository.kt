package dev.oskarjohansson.respository

import dev.oskarjohansson.domain.model.Author
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository: MongoRepository<Author, String> {

    fun findByAuthorName(author: String): Author?
    fun findByAuthorId(authorId: String): Author?
}
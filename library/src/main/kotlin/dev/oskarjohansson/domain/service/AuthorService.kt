package dev.oskarjohansson.domain.service


import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.respository.AuthorRepository
import org.springframework.stereotype.Service


@Service
class AuthorService(private val authorRepository: AuthorRepository) {

    fun save(authorName: String): Author {
        authorRepository.findByAuthorName(authorName)?.let {
            throw IllegalStateException("Author already exist, ${it.authorName} ")
        }

        return authorRepository.save(Author(null, authorName, null))
    }
}
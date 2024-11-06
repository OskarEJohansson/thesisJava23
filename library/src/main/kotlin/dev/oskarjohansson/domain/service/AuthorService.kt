package dev.oskarjohansson.domain.service


import dev.oskarjohansson.api.dto.AuthorResponseDTO
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.respository.AuthorRepository
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException


@Service
class AuthorService(private val authorRepository: AuthorRepository) {

    fun save(authorName: String): Author {
        authorRepository.findByAuthorName(authorName)?.let {
            throw IllegalArgumentException("Author already exist, ${it.authorName}, id: ${it.authorId} ")
        }

        return authorRepository.save(Author(authorName = authorName))
    }

    fun getOrCreateAuthor(authorName: String): String? {
        return authorRepository.findByAuthorName(authorName)?.authorId
            ?: authorRepository.save(Author(authorName = authorName)).authorId
    }

    fun createAuthorResponseDTO(authors: List<String>): List<AuthorResponseDTO> {

        // TODO: Try to refactor this to remove !! in the authorResponseDTO
        return authors.map { authorId ->
            authorRepository.findById(authorId).orElse(null)
                ?.takeIf { it.authorId != null }
                ?.let {
                    AuthorResponseDTO(it.authorId!!, it.authorName)
                } ?: throw IllegalStateException("Author with id ${authorId} not found or Id is null.")
        }
    }

}


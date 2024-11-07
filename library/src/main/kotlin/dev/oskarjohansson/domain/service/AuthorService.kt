package dev.oskarjohansson.domain.service


import dev.oskarjohansson.api.dto.AuthorResponseDTO
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.respository.AuthorRepository
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import kotlin.jvm.optionals.getOrNull


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

    // TODO: Write tests
    fun createAuthorResponseDTO(authors: List<String>): List<AuthorResponseDTO> {

        return authors.mapNotNull { authorId ->
            authorRepository.findById(authorId).getOrNull()
                ?.let {
                    AuthorResponseDTO(it.authorId!!, it.authorName)
                }
        }
    }

}


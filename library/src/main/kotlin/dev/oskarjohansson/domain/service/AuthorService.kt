package dev.oskarjohansson.domain.service


import dev.oskarjohansson.api.dto.AuthorInBookResponseDTO
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.respository.AuthorRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull


@Service
class AuthorService(private val authorRepository: AuthorRepository) {

    fun saveAuthor(authorName: String): Author {
        return findExistingAuthor(authorName)
            ?: authorRepository.save(Author(authorName = authorName))
    }

    fun findExistingAuthor(authorName: String): Author? =
        authorRepository.findByAuthorName(authorName)

    fun createAuthorResponseDTO(listOfAuthorIds: List<String>): List<AuthorInBookResponseDTO> {
        return listOfAuthorIds.mapNotNull { authorId ->
            authorRepository.findById(authorId).getOrNull()?.let {
                AuthorInBookResponseDTO(it.authorId!!, it.authorName)
            }
        }
    }

    fun getOrCreateAuthors(authors: List<String>): List<Author> {
        return authors.map {
            authorRepository.findByAuthorName(it)
                ?: authorRepository.save(Author(authorName = it))
        }
    }

    fun getAuthors(pageable: Pageable): Page<Author> {
        return authorRepository.findAll(pageable)
    }
}


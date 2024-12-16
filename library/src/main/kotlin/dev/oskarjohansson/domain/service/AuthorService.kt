package dev.oskarjohansson.domain.service


import dev.oskarjohansson.api.dto.request.AddAuthorRequestDTO
import dev.oskarjohansson.api.dto.response.AuthorInBookResponseDTO
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.respository.AuthorRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull


@Service
class AuthorService(private val authorRepository: AuthorRepository) {

    fun saveAuthor(authorName: String): Author =
        authorRepository.save(Author(authorName = authorName))

    fun findAuthorByName(authorName: String): Author? =
        authorRepository.findByAuthorName(authorName)

    fun findAuthorById(authorId: String): Author? =
        authorRepository.findByAuthorId(authorId)

    fun createAuthorInBookResponseDTO(listOfAuthorIds: List<String>): List<AuthorInBookResponseDTO> {
        return listOfAuthorIds.mapNotNull { authorId ->
            authorRepository.findById(authorId).getOrNull()?.let {
                AuthorInBookResponseDTO(it.authorId!!, it.authorName)
            }
        }
    }

    fun getOrCreateAuthor(author: AddAuthorRequestDTO): Author {
        return author.authorName?.let { findAuthorByName(it) }
            ?: author.authorId?.let { findAuthorById(it) }
            ?: saveAuthor(author.authorName!!) //Null check in controller
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


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

    // TODO: move logic to library service 
    fun saveAuthor(authorName: String): Author {
        return findAuthorByName(authorName)
            ?: authorRepository.save(Author(authorName = authorName))
    }

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
    
    fun getOrCreateAuthor(author: AddAuthorRequestDTO): Author{
        findAuthorByName(author.authorName) ?: findAuthorById(author.authorId) ?: saveAuthor(author)
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


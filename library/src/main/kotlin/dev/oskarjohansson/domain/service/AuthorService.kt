package dev.oskarjohansson.domain.service


import dev.oskarjohansson.api.dto.AuthorInBookResponseDTO
import dev.oskarjohansson.api.dto.AuthorResponseDTO
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.respository.AuthorRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import kotlin.IllegalStateException
import kotlin.jvm.optionals.getOrNull


@Service
class AuthorService(private val authorRepository: AuthorRepository) {

    fun saveAuthor(authorName: String): Author {
        return findExistingAuthor(authorName)
            ?: authorRepository.save(Author(authorName = authorName))
            ?: throw IllegalStateException("Failed to find or create author $authorName")
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

    fun getOrCreateAuthor(authorName: String): String =
        authorRepository.findByAuthorName(authorName)?.authorId
            ?: authorRepository.save(Author(authorName = authorName)).authorId
            ?: throw IllegalStateException("Failed to create or retrieve Author $authorName")






    fun getAuthors(pageable: Pageable): Page<Author> {
    return authorRepository.findAll(pageable)
    }


//    .map { author ->
//        author.authorId?.let { authorId ->
//            author.toAuthorResponseDTO(createBookInAuthorResponseDTO(authorId))
//        }
//    }
}


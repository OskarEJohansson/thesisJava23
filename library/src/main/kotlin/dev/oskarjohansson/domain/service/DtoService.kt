package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.AuthorInBookResponseDTO
import dev.oskarjohansson.api.dto.BookInAuthorResponseDTO
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.respository.AuthorRepository
import dev.oskarjohansson.respository.BookRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class DtoService(private val authorRepository: AuthorRepository, private val bookRepository: BookRepository) {

    fun createAuthorResponseDTO(listOfAuthorIds: List<String>): List<AuthorInBookResponseDTO> {

        return listOfAuthorIds.mapNotNull { authorId ->
            authorRepository.findById(authorId).getOrNull()?.let {
                AuthorInBookResponseDTO(it.authorId!!, it.authorName)
            }
        }
    }

    fun createBookInAuthorResponseDTO(authorId: String): List<BookInAuthorResponseDTO> {

        return bookRepository.findByAuthorIds(authorId)?.map { book ->
            BookInAuthorResponseDTO(book.bookId!!, book.title)
        } ?: listOf(BookInAuthorResponseDTO("0", "No published books"))

    }

    fun getOrCreateAuthor(authorName: String): String? {
        return authorRepository.findByAuthorName(authorName)?.authorId
            ?: authorRepository.save(Author(authorName = authorName)).authorId
    }
}
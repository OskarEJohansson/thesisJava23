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

    fun createBookInAuthorResponseDTO(publishedBooksId: List<String>?): List<BookInAuthorResponseDTO> {

        // TODO: put up side down, take authorID and find in book repository and filter as all authors has 0 books in its entity 
        return publishedBooksId?.mapNotNull { bookId ->
            bookRepository.findById(bookId).getOrNull()?.let {
                BookInAuthorResponseDTO(it.bookId!!, it.title)
            }
        } ?: listOf(BookInAuthorResponseDTO("0", "No published books"))
    }

    fun getOrCreateAuthor(authorName: String): String? {
        return authorRepository.findByAuthorName(authorName)?.authorId
            ?: authorRepository.save(Author(authorName = authorName)).authorId
    }
}
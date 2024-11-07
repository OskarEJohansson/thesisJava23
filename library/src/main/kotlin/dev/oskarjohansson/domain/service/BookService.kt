package dev.oskarjohansson.domain.service


import dev.oskarjohansson.api.dto.BookRequestDTO
import dev.oskarjohansson.api.dto.BookResponseDTO
import dev.oskarjohansson.domain.model.Book
import dev.oskarjohansson.respository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.lang.IllegalStateException

@Service
class BookService(private val bookRepository: BookRepository, private val authorService: AuthorService) {

    fun saveBook(book: BookRequestDTO): Book {
        return authorService.getOrCreateAuthor(book.authorName)?.let {
            bookRepository.save(Book(authorIds = listOf(it), title = book.title, genres = book.genre))
        } ?: throw java.lang.IllegalStateException("Failed to create or retrieve author: ${book.authorName}")
    }

    fun findBookById(bookId: String): Boolean {
        return bookRepository.findById(bookId).isPresent
    }

    fun getBooks(pageable: Pageable): Page<BookResponseDTO> {
        return bookRepository.findAll(pageable).map { book ->
            book
                .toBookResponseDTO(
                    authorService.createAuthorResponseDTO(book.authorIds)
                )
        }
    }
}

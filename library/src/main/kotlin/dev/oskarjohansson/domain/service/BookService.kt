package dev.oskarjohansson.domain.service


import dev.oskarjohansson.api.dto.BookInAuthorResponseDTO
import dev.oskarjohansson.api.dto.BookRequestDTO
import dev.oskarjohansson.api.dto.BookResponseDTO
import dev.oskarjohansson.domain.model.Book
import dev.oskarjohansson.respository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookService(private val bookRepository: BookRepository) {

    fun saveBook(book: BookRequestDTO, authorId: String): Book {
        return bookRepository.save(Book(authorIds = listOf(authorId), title = book.title, genres = book.genre))
        ?: throw IllegalStateException("Failed to create or retrieve author: ${book.authorName}")
    }

    fun findBookById(bookId: String): Boolean {
        return bookRepository.findById(bookId).isPresent
    }

    fun findAllBooksPageable(pageable: Pageable) : Page<Book> = bookRepository.findAll(pageable)

    // TODO: THE NO PUBLISHED BOOKS FUNCTION DOES NOT WORK 
    fun createBookInAuthorResponseDTO(authorId: String): List<BookInAuthorResponseDTO> {
        return bookRepository.findByAuthorIds(authorId)?.map { book ->
            BookInAuthorResponseDTO(book.bookId!!, book.title)
        } ?: listOf(BookInAuthorResponseDTO("0", "No published books"))

    }
}

package dev.oskarjohansson.domain.service


import dev.oskarjohansson.api.dto.BookInAuthorResponseDTO
import dev.oskarjohansson.api.dto.BookRequestDTO
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.domain.model.Book
import dev.oskarjohansson.respository.BookRepository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException


@Service
class BookService(private val bookRepository: BookRepository) {

    fun saveBook(bookRequest: BookRequestDTO, authorId: List<Author>): Book {
        return bookRepository.save(bookRequest.toBook())
    }

    fun findBookById(bookId: String): Book {
        return bookRepository.findByBookId(bookId) ?: throw IllegalArgumentException("Could not find book with id $bookId")
    }

    fun findAllBooksPageable(pageable: Pageable): Page<Book> = bookRepository.findAll(pageable)

    fun createBookInAuthorResponseDTO(authorId: String): List<BookInAuthorResponseDTO>? {
        return bookRepository.findByAuthorIds(authorId)?.map { book ->
            BookInAuthorResponseDTO(book.bookId!!, book.title)
        }
    }
}

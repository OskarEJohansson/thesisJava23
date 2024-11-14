package dev.oskarjohansson.domain.service


import dev.oskarjohansson.api.dto.BookInAuthorResponseDTO
import dev.oskarjohansson.api.dto.BookRequestDTO
import dev.oskarjohansson.api.dto.BookResponseDTO
import dev.oskarjohansson.api.dto.RegisterBookRequestDTO
import dev.oskarjohansson.domain.model.Book
import dev.oskarjohansson.respository.BookRepository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException


@Service
class BookService(private val bookRepository: BookRepository) {

    fun saveBook(bookRequest: RegisterBookRequestDTO, authors: List<String>): Book {
        return bookRepository.save(bookRequest.toBook(authors))
    }

    fun findBookById(bookId: String): Book {
        return bookRepository.findByBookId(bookId)
            ?: throw IllegalArgumentException("Could not find book with id $bookId")
    }

    fun findBookByIdOrTitle(bookRequestDTO: BookRequestDTO): Book {
        return bookRequestDTO.title?.let {
            bookRepository.findByTitle(it)
        } ?: bookRequestDTO.bookId?.let {
            bookRepository.findByBookId(it)
        } ?: throw IllegalStateException("Could not find book")
    }


fun findAllBooksPageable(pageable: Pageable): Page<Book> = bookRepository.findAll(pageable)

// TODO: books in author does not work
fun createBookInAuthorResponseDTO(authorId: String): List<BookInAuthorResponseDTO>? {
    return bookRepository.findByAuthorIds(authorId)?.map { book ->
        BookInAuthorResponseDTO(book.bookId!!, book.title)
    }
}
}

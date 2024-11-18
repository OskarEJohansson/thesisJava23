package dev.oskarjohansson.domain.service


import dev.oskarjohansson.api.dto.response.BookInAuthorResponseDTO
import dev.oskarjohansson.api.dto.request.BookRequestDTO
import dev.oskarjohansson.api.dto.request.RegisterBookRequestDTO
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

    fun saveBookWithNewAuthor(book: Book): Book {
        return bookRepository.save(book)
    }

    fun findBookById(bookId: String): Book {
        return bookRepository.findByBookId(bookId)
            ?: throw IllegalArgumentException("Could not find book with id $bookId")
    }

    fun validateAuthorExistenceInBook(bookId: String, authorId: String): Unit {
        val isPresent = bookRepository.findByBookId(bookId)?.authorIds?.contains(authorId)
        if (isPresent == true) {
            throw IllegalArgumentException("Author $authorId already exist in authorIds in book $bookId")
        }
    }

    fun findBookByIdOrTitle(bookRequestDTO: BookRequestDTO): Book {
        return bookRequestDTO.title?.let {
            bookRepository.findByTitle(it)
        } ?: bookRequestDTO.bookId?.let {
            bookRepository.findByBookId(it)
        }
        ?: throw IllegalStateException("Could not find book with id ${bookRequestDTO.bookId} or title ${bookRequestDTO.title}")
    }

    fun findAllBooksPageable(pageable: Pageable): Page<Book> = bookRepository.findAll(pageable)

    fun createBookInAuthorResponseDTO(authorId: String): List<BookInAuthorResponseDTO>? {
        return bookRepository.findByAuthorIds(listOf(authorId))?.map { book ->
            BookInAuthorResponseDTO(book.bookId!!, book.title)
        }
    }
}

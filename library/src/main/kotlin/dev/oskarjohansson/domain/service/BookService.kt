package dev.oskarjohansson.domain.service


import dev.oskarjohansson.api.dto.BookRequestDTO
import dev.oskarjohansson.api.dto.BookResponseDTO
import dev.oskarjohansson.domain.model.Book
import dev.oskarjohansson.respository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookService(private val bookRepository: BookRepository, private val authorService: AuthorService) {

    fun saveBook(book: BookRequestDTO): Book {

        return runCatching {
            val authorID = authorService.getOrCreateAuthor(book.authorName)
                ?: throw IllegalStateException(
                    "Failed to create or retrieve author: ${book.authorName}}"
                )

            bookRepository.save(
                Book(
                    title = book.title,
                    authorIds = listOf(authorID),
                    genres = book.genre
                )
            )
        }.getOrThrow()
    }

    fun findBookById(bookId: String): Boolean {
        return bookRepository.findById(bookId).isPresent
    }

    fun getBooks(pageable: Pageable): Page<BookResponseDTO> {
        // TODO: finish map and find out how to return a page with the bookResponseDTO
        val books = bookRepository.findAll(pageable)
        return books.map { book -> book.toBookResponseDTO(authorService.createAuthorResponseDTO(book.authorIds)) }
    }
}
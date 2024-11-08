package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.AuthorResponseDTO
import dev.oskarjohansson.api.dto.BookRequestDTO
import dev.oskarjohansson.api.dto.BookResponseDTO
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.domain.model.Book
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class LibraryService(

    private val authorService: AuthorService,
    private val bookService: BookService,
    private val reviewService: ReviewService
) {

    // TODO: REFACTOR WHEN ALL WORKS 
    fun saveBook(book: BookRequestDTO): BookResponseDTO {
        val authorId = authorService.getOrCreateAuthor(book.authorName)
        val savedBook = bookService.saveBook(book, authorId)
        val authors = authorService.createAuthorResponseDTO(savedBook.authorIds)
        return savedBook.toBookResponseDTO(authors)
    }

    fun getBooks(pageable: Pageable): Page<BookResponseDTO> {

        return runCatching {
            bookService.findAllBooksPageable(pageable).map { book ->
                book.takeIf { book.bookId != null }
                book
                    .toBookResponseDTO(
                        authorService.createAuthorResponseDTO(book.authorIds)
                    )
            }
        }.getOrElse { throw IllegalStateException("Error while loading books: ${it.message}") }
    }


    fun saveAuthor(authorName: String): Author {
        return authorService.saveAuthor(authorName)
    }


    fun getAuthors(pageable: Pageable): Page<AuthorResponseDTO> {
        val authors = authorService.getAuthors(pageable)

        return authors.map { author ->
            author.authorId?.let { authorId ->
                author.toAuthorResponseDTO(bookService.createBookInAuthorResponseDTO(authorId))

            }
        }
    }
}
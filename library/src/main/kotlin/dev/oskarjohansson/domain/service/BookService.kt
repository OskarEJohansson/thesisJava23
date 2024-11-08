package dev.oskarjohansson.domain.service


import dev.oskarjohansson.api.dto.BookRequestDTO
import dev.oskarjohansson.api.dto.BookResponseDTO
import dev.oskarjohansson.domain.model.Book
import dev.oskarjohansson.respository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookService(private val bookRepository: BookRepository, private val dtoService: DtoService) {

    fun saveBook(book: BookRequestDTO): Book {
        return dtoService.getOrCreateAuthor(book.authorName)?.let {
            bookRepository.save(Book(authorIds = listOf(it), title = book.title, genres = book.genre))
        } ?: throw IllegalStateException("Failed to create or retrieve author: ${book.authorName}")
    }

    fun findBookById(bookId: String): Boolean {
        return bookRepository.findById(bookId).isPresent
    }

    fun getBooks(pageable: Pageable): Page<BookResponseDTO> {

        return runCatching {
            bookRepository.findAll(pageable).map { book ->
                takeIf { book.bookId != null }
                book
                    .toBookResponseDTO(
                        dtoService.createAuthorResponseDTO(book.authorIds)
                    )
            }
        }.getOrElse { throw IllegalStateException("Error while loading books: ${it.message}") }
    }

//    fun createBookInAuthorResponseDTO(publishedBooksId: List<String>?): List<BookInAuthorResponseDTO> {
//
//        return publishedBooksId?.mapNotNull { bookId ->
//            bookRepository.findById(bookId).getOrNull()?.let {
//                BookInAuthorResponseDTO(it.bookId!!, it.title)
//            }
//        } ?: listOf(BookInAuthorResponseDTO("0", "No published books"))
//    }
}

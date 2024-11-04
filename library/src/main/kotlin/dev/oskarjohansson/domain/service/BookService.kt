package dev.oskarjohansson.domain.service


import dev.oskarjohansson.api.dto.BookDTO
import dev.oskarjohansson.domain.model.Book
import dev.oskarjohansson.respository.BookRepository
import org.springframework.stereotype.Service

@Service
class BookService(private val bookRepository: BookRepository, private val authorService: AuthorService) {

    fun saveBook(book: BookDTO): Book {

        return runCatching {
            val authorID = authorService.getOrCreateAuthor(book.authorName)
                ?: throw IllegalStateException(
                    "Failed to create or retrieve author: ${book.authorName}}")

            bookRepository.save(
                Book(title = book.title,
                    authorIds = listOf(authorID),
                    genres = book.genre))
        }.getOrThrow()
    }
}
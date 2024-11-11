package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.AuthorResponseDTO
import dev.oskarjohansson.api.dto.BookRequestDTO
import dev.oskarjohansson.api.dto.BookResponseDTO
import dev.oskarjohansson.api.dto.ReviewDTO
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.domain.model.Book
import dev.oskarjohansson.domain.model.Review
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

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

        // TODO: Refactor two in a row let?
        return authors.map { author ->
            author.authorId?.let { authorId ->
                bookService.createBookInAuthorResponseDTO(authorId)?.let {
                    author.toAuthorResponseDTO(it)
                }
            }
        }
    }

    /* TODO: FIND OUT WHY MESSAGE WHEN REVIEW ALREADY EXIST LOOKS LIKE THIS:
    Expected to read Document Document{{_id=6729ec9e6b5c99640509fade,
    text=This is a great book!, rating=5, createdAt=Tue Nov 05 10:59:58 CET 2024, userId=6728ca2d572c957e4386f8e8, bookId=6729eb38c14eae0ffd536ac0,
    _class=dev.oskarjohansson.domain.model.Review}} into type boolean but didn't find a PersistentEntity for the latter",
     */

    fun createReview(review: ReviewDTO, jwt: Jwt): Review {
        return run{

            // TODO: A Check that userID exist
            // TODO: B Check that book exist
            // TODO: C Check If user has an existing review on Book
            // TODO: If C is true, return review Id else continue
            // TODO: Create review with reviewDTO and userId string
            // TODO:  propagate Error to controller

            jwt.claims["userId"]?.toString()
                ?.takeIf { bookService.findBookById(review.bookId)}
                ?.takeIf { !reviewService.findReviewCreatedByUser(userId = it) }
                ?.let { reviewService.createReview(review, it) }
                ?: throw IllegalArgumentException("Could not find book with bookId: ${review.bookId}")
        }

    }
}
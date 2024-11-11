package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.*
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

    fun getBook(bookId: String): Book = bookService.findBookById(bookId)



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

    fun createReview(review: ReviewDTO, jwt: Jwt): Review {
        return run {

            // TODO: A Check that book exist
            // TODO: B Check that userID exist
            // TODO: C Check If user has an existing review on Book
            // TODO: If C is true, return review Id else continue
            // TODO: Create review with reviewDTO and userId string
            // TODO:  propagate Error to controller

            run {
                bookService.findBookById(review.bookId)
                jwt.claims["userId"]?.toString()
                    ?.takeIf { !reviewService.findReviewCreatedByUser(userId = it) }
                    ?.let { reviewService.createReview(review, it) }
                    ?: throw IllegalArgumentException("Could not find book with bookId: ${review.bookId}")
            }

        }

    }

    fun getReviews(pageable: Pageable, bookId: String): Page<ReviewResponseDTO> {

        // TODO: A Find book and do a null check
        // TODO: B Send list of reviewIds to reviewService
        // TODO: Map reviewIdList and fetch review
        // TODO: Convert Review to ReviewResponseDTO
        // TODO: Collect in a Page<ReviewResponseDTO>

        // TODO: CANT FIND BOOK ON ID
        return bookService.findBookById(bookId).let { book ->
            reviewService.createPageableReviews(pageable, book.bookId!!)
        }
    }


}
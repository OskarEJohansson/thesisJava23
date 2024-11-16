package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.request.BookRequestDTO
import dev.oskarjohansson.api.dto.request.RegisterBookRequestDTO
import dev.oskarjohansson.api.dto.request.ReviewRequestDTO
import dev.oskarjohansson.api.dto.response.AuthorInBookResponseDTO
import dev.oskarjohansson.api.dto.response.BookResponseDTO
import dev.oskarjohansson.domain.enums.Genres
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.domain.model.Book
import dev.oskarjohansson.domain.model.Review
import io.ktor.util.reflect.*
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.security.oauth2.jwt.Jwt
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class LibraryServiceTest {

    private val authorService = mockk<AuthorService>()
    private val bookService = mockk<BookService>()
    private val reviewService = mockk<ReviewService>()
    private val libraryService = LibraryService(authorService, bookService, reviewService)
    private val jwt = mockk<Jwt>()
    private val book = Book("1234", "Book1", listOf("1234", "1235"), Genres.FANTASY)
    private val review = Review("12345", "Review 123", 5, LocalDateTime.now(), null, "user123", "1234")

    private val reviewRequestDto = ReviewRequestDTO("Review 123", 5, "1234")


    @Test
    fun `assertDoesNotThrow that createReview does not throw exception with valid inputs`() {

        every { jwt.claims } returns mapOf("userId" to "user123")
        every { bookService.findBookById(any()) } returns book
        every { reviewService.findByBookIdAndUserId(any(), any()) } returns null
        every { reviewService.createReview(any(), any()) } returns review
        assertDoesNotThrow { libraryService.saveReview(reviewRequestDto, jwt) }
    }

    @Test
    fun `test that createReview throws exception when user has an existing review`() {

        every { jwt.claims } returns mapOf("userId" to "user123")
        every { bookService.findBookById(any()) } returns book
        every { reviewService.findByBookIdAndUserId(any(), any()) } returns review
        assertThrows<IllegalArgumentException> { libraryService.saveReview(reviewRequestDto, jwt) }
    }


    @Test
    fun `test that createReview throws IllegalArgumentException when user already has registered a review`() {

        every { bookService.findBookById(any()) } returns book
        every { reviewService.findByBookIdAndUserId(any(), any()) } returns review
        every { jwt.claims } returns mapOf("userId" to "user123")
        assertThrows<IllegalArgumentException> { libraryService.saveReview(reviewRequestDto, jwt) }
    }

    @Test
    fun `test that createReview does not throw IllegalArgumentException when user has no review registered`() {

        every { bookService.findBookById(any()) } returns book
        every { reviewService.findByBookIdAndUserId(any(), any()) } returns null
        every { jwt.claims } returns mapOf("userId" to "user123")
        every { reviewService.createReview(any(), any()) } returns review
        assertDoesNotThrow{ libraryService.saveReview(reviewRequestDto, jwt) }
    }

    @Test
    fun `test that createReview throws IllegalArgumentException when Book is not found`() {

        every { bookService.findBookById(any()) } throws IllegalStateException()
        every { reviewService.findByBookIdAndUserId(any(), any()) } returns null
        every { jwt.claims } returns mapOf("userId" to "user123")
        every { reviewService.createReview(any(), any()) } returns review
        assertThrows<IllegalStateException>{ libraryService.saveReview(reviewRequestDto, jwt) }
    }

    @Test
    fun `test that createReview throws IllegalArgumentException review can't be persisted`() {
        every { bookService.findBookById(any()) } returns book
        every { reviewService.findByBookIdAndUserId(any(), any()) } returns null
        every { jwt.claims } returns mapOf("userId" to "user123")
        every { reviewService.createReview(any(), any()) } throws IllegalStateException()
        assertThrows<IllegalStateException>{ libraryService.saveReview(reviewRequestDto, jwt) }
    }

    @Test
    fun`Test saveBook`(){

        every { authorService.getOrCreateAuthors(any()) } returns listOf(Author("Author", "Author"))
        every { bookService.saveBook(any(), any()) } returns Book("New Book", "New Book", listOf("Author"), Genres.FANTASY)
        every { authorService.createAuthorResponseDTO(any()) } returns listOf(AuthorInBookResponseDTO("Author", "Author"))
        val bookRequest = RegisterBookRequestDTO("Book",listOf("Author"), Genres.FANTASY)

        assertDoesNotThrow { libraryService.saveBook(bookRequest) }
    }

    @Test
    fun `test that save Author does not throw error when converting Author to AuthorResponseDTO`(){
        every { authorService.saveAuthor(any()) } returns Author("AuthorId", "Author")

        val authorDto = libraryService.saveAuthor("Author")

        assertEquals("AuthorId", authorDto.authorID)
        assertEquals("Author", authorDto.authorName)
        assertDoesNotThrow { libraryService.saveAuthor("Author") }
    }


}
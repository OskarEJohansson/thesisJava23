package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.request.AddAuthorRequestDTO
import dev.oskarjohansson.api.dto.request.BookRequestDTO
import dev.oskarjohansson.api.dto.request.RegisterBookRequestDTO
import dev.oskarjohansson.api.dto.request.ReviewRequestDTO
import dev.oskarjohansson.api.dto.response.AuthorInBookResponseDTO
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
    fun `assertDoesNotThrow that saveReview does not throw exception with valid inputs`() {

        every { jwt.claims } returns mapOf("userId" to "user123")
        every { bookService.findBookById(any()) } returns book
        every { reviewService.findByBookIdAndUserId(any(), any()) } returns null
        every { reviewService.createReview(any(), any()) } returns review
        assertDoesNotThrow { libraryService.saveReview(reviewRequestDto, jwt) }
    }

    @Test
    fun `test that saveReview throws exception when user has an existing review`() {

        every { jwt.claims } returns mapOf("userId" to "user123")
        every { bookService.findBookById(any()) } returns book
        every { reviewService.findByBookIdAndUserId(any(), any()) } returns review
        assertThrows<IllegalArgumentException> { libraryService.saveReview(reviewRequestDto, jwt) }
    }


    @Test
    fun `test that saveReview throws IllegalArgumentException when user already has registered a review`() {

        every { bookService.findBookById(any()) } returns book
        every { reviewService.findByBookIdAndUserId(any(), any()) } returns review
        every { jwt.claims } returns mapOf("userId" to "user123")
        assertThrows<IllegalArgumentException> { libraryService.saveReview(reviewRequestDto, jwt) }
    }

    @Test
    fun `test that saveReview does not throw IllegalArgumentException when user has no review registered`() {

        every { bookService.findBookById(any()) } returns book
        every { reviewService.findByBookIdAndUserId(any(), any()) } returns null
        every { jwt.claims } returns mapOf("userId" to "user123")
        every { reviewService.createReview(any(), any()) } returns review
        assertDoesNotThrow { libraryService.saveReview(reviewRequestDto, jwt) }
    }

    @Test
    fun `test that saveReview throws IllegalArgumentException when Book is not found`() {

        every { bookService.findBookById(any()) } throws IllegalStateException()
        every { reviewService.findByBookIdAndUserId(any(), any()) } returns null
        every { jwt.claims } returns mapOf("userId" to "user123")
        every { reviewService.createReview(any(), any()) } returns review
        assertThrows<IllegalStateException> { libraryService.saveReview(reviewRequestDto, jwt) }
    }

    @Test
    fun `test that saveReview throws IllegalArgumentException review can't be persisted`() {
        every { bookService.findBookById(any()) } returns book
        every { reviewService.findByBookIdAndUserId(any(), any()) } returns null
        every { jwt.claims } returns mapOf("userId" to "user123")
        every { reviewService.createReview(any(), any()) } throws IllegalStateException()
        assertThrows<IllegalStateException> { libraryService.saveReview(reviewRequestDto, jwt) }
    }

    @Test
    fun `Test saveBook`() {

        every { authorService.getOrCreateAuthors(any()) } returns listOf(Author("Author", "Author"))
        every { bookService.saveBook(any(), any()) } returns Book(
            "New Book",
            "New Book",
            listOf("Author"),
            Genres.FANTASY
        )
        every { authorService.createAuthorInBookResponseDTO(any()) } returns listOf(
            AuthorInBookResponseDTO(
                "Author",
                "Author"
            )
        )
        val bookRequest = RegisterBookRequestDTO("Book", listOf("Author"), Genres.FANTASY)

        assertDoesNotThrow { libraryService.saveBook(bookRequest) }
    }

    @Test
    fun `test that save Author does not throw error when converting Author to AuthorResponseDTO`() {
        every { authorService.saveAuthor(any()) } returns Author("AuthorId", "Author")
        every { authorService.findAuthorByName(any()) } returns null

        val authorDto = libraryService.saveAuthor("Author")

        assertEquals("AuthorId", authorDto.authorID)
        assertEquals("Author", authorDto.authorName)
        assertDoesNotThrow { libraryService.saveAuthor("Author") }
    }

    @Test
    fun `test that saveAuthor throws error when author exist`(){
        every { authorService.findAuthorByName(any()) } returns Author("123", "ABC")
        every { authorService.saveAuthor(any()) } returns Author("AuthorId", "Author")

        assertThrows<IllegalArgumentException> { libraryService.saveAuthor("author")  }
    }

    @Test
    fun `test that getBookByIdOrTitle logical flow `() {
        every { bookService.findBookByIdOrTitle(any()) } returns book
        every { authorService.createAuthorInBookResponseDTO(any()) } returns listOf(
            AuthorInBookResponseDTO(
                "AuthorId",
                "Author"
            )
        )
        val bookRequest = BookRequestDTO("BookId")
        val bookResponse = libraryService.getBookByIdOrTitle(bookRequest)

        assertTrue { bookResponse.authors!![0].instanceOf(AuthorInBookResponseDTO::class) }
        assertDoesNotThrow { libraryService.getBookByIdOrTitle(bookRequest) }
    }

    @Test
    fun `test getBooks-Authors-Reviews`() {
// TODO: research testing pageable

    }

    @Test
    fun `test that deleteReview does not throw error when userId matches`(){
        val time = LocalDateTime.now()
        val review = Review(
            "ReviewId",
            "Review Text",
            5,
            time,
            null,
            "UserId",
            "BookId"
        )
        every {reviewService.findById(any())} returns review
        every { jwt.claims } returns mapOf("userId" to "UserId")
        every { reviewService.deleteById(any()) } returns Unit

        assertDoesNotThrow { libraryService.deleteReview(jwt, reviewId = "123") }
    }

    @Test
    fun `test that deleteReview throws error when userId does not match`(){
        val time = LocalDateTime.now()
        val review = Review(
            "ReviewId",
            "Review Text",
            5,
            time,
            null,
            "UserId",
            "BookId"
        )
        every {reviewService.findById(any())} returns review
        every { jwt.claims } returns mapOf("userId" to null)
        every { reviewService.deleteById(any()) } returns Unit

        assertThrows<IllegalArgumentException> { libraryService.deleteReview(jwt, reviewId = "123") }
    }

    @Test
    fun `test updateReview does not throw error when isUser is true`() {
        val time = LocalDateTime.now()
        val review = Review(
            "ReviewId",
            "Review Text",
            5,
            time,
            null,
            "UserId",
            "BookId"
        )

        val request = ReviewRequestDTO("Updated Text", 5, reviewId = "123")
        every { reviewService.findById(any()) } returns review
        every { jwt.claims } returns mapOf("userId" to "UserId")
        every { reviewService.save(any()) } returns review
        assertDoesNotThrow { libraryService.updateReview(jwt, request) }
    }




    @Test
    fun `test updateReview throws error when isUser is false`() {
        val time = LocalDateTime.now()
        val review = Review(
            "ReviewId",
            "Review Text",
            5,
            time,
            null,
            "UserId",
            "BookId"
        )

        val request = ReviewRequestDTO("Updated Text", 5, reviewId = "123")
        every { reviewService.findById(any()) } returns review
        every { jwt.claims } returns mapOf("userId" to null)
        every { reviewService.save(any()) } returns review
        assertThrows<IllegalArgumentException> { libraryService.updateReview(jwt, request) }
    }

    @Test
    fun`test addAuthor throws no errors with valid inputs`(){
        val addAuthor = AddAuthorRequestDTO("123", "ABC")
        val bookWithOneAuthor = Book("123", "Book", listOf("123ABC"),Genres.FANTASY)
        val bookWithTwoAuthor = Book("123", "Book", listOf("ABC","123ABC"),Genres.FANTASY)
        val authorsDTO = listOf(AuthorInBookResponseDTO("ABC", "Author"), AuthorInBookResponseDTO("ABC123", "Author"))

        every {bookService.findBookById(addAuthor.bookId)} returns bookWithOneAuthor
        every {authorService.getOrCreateAuthor(addAuthor) } returns Author("ABC123", "ABC")
        every {bookService.validateAuthorExistenceInBook(any(),any())} returns Unit
        every {bookService.saveBookWithNewAuthor(any())} returns bookWithTwoAuthor
        every {authorService.createAuthorInBookResponseDTO(any()) } returns authorsDTO

        assertDoesNotThrow { libraryService.addAuthor(addAuthor) }
    }

}
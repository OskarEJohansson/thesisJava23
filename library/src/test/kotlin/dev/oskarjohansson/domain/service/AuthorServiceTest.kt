package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.AuthorInBookResponseDTO
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.respository.AuthorRepository
import io.ktor.util.reflect.*
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.*

class AuthorServiceTest {

    private val authorRepository = mockk<AuthorRepository>()
    private val authorService = AuthorService(authorRepository)
    private val author = "Author"
    private val existingAuthor = mockk<Author> {
        every { authorName } returns author
        every { authorId } returns "Author123"
    }
    private val listOfTwoAuthors = listOf("1234", "12354")


    @Test
    fun `Test that saveAuthor return existing author if author exist `() {
        every { authorRepository.findByAuthorName(any()) } returns existingAuthor
        assertEquals(existingAuthor, authorService.saveAuthor(author))
    }

    @Test
    fun `Test that save does not throw error if author does not exist`() {
        every { authorRepository.findByAuthorName(any()) } returns null
        every { authorRepository.save(any()) } returns mockk<Author>()
        assertDoesNotThrow { authorService.saveAuthor(author) }
    }

    @Test
    fun `that getOrCreateAuthor returns id when an author exist`() {
        every { authorRepository.findByAuthorName(any()) } returns existingAuthor
        assertEquals(existingAuthor.authorId, authorService.getOrCreateAuthor("author"))
    }

    @Test
    fun `test that getOrCreateAuthor saves a new author when author does not exist in the repository`() {
        every { authorRepository.findByAuthorName(any()) } returns null
        every { authorRepository.save(any()) } returns Author("123", "author2")

        assertEquals("123", authorService.getOrCreateAuthor("NAME"))
    }

    @Test
    fun `test that createAuthorResponseDTO throws error if no Author is found`() {
        every { authorRepository.findById(any()) } returns Optional.empty()

        assertDoesNotThrow { authorService.createAuthorResponseDTO(listOfTwoAuthors) }
    }


    @Test
    fun `test that createAuthorResponseDTO returns a AuthorResponseDTO when called with one author`() {
        val author = Author("1234", "AuthorSuccess")
        val authorDto = AuthorInBookResponseDTO("1234", "AuthorSuccess")

        every { authorRepository.findById(any()) } returns Optional.of(author)

        val response = authorService.createAuthorResponseDTO(listOf("1234"))

        assertTrue { response[0].instanceOf(AuthorInBookResponseDTO::class) }
        assertTrue { response.size == 1 }
    }


    @Test
    fun `test that createAuthorResponseDTO returns a AuthorResponseDTO when called with many authors`() {
        val author = Author("1234", "AuthorSuccess")
        val authorDto = listOf(
            AuthorInBookResponseDTO(
                "1234", "AuthorSuccess"
            ), AuthorInBookResponseDTO("23456", "AnotherSuccess")
        )
        every { authorRepository.findById(any()) } returns Optional.of(author)

        assertDoesNotThrow { authorService.createAuthorResponseDTO(listOfTwoAuthors) }
        assertTrue { authorService.createAuthorResponseDTO(listOfTwoAuthors).size == 2 }

    }

    @Test
    fun `test that createAuthorResponseDTO returns a list of 2 object when called with a a list of 3 with 1 partial failure`() {
        val authorsPartFailure = listOf("1234", "Invalid", "2345")
        val author = Author("1234", "AuthorSuccess")
        val anotherAuthor = Author("2345", "AuthorSuccess")

        every { authorRepository.findById("1234") } returns Optional.of(author)
        every { authorRepository.findById("Invalid") } returns Optional.empty()
        every { authorRepository.findById("2345") } returns Optional.of(anotherAuthor)

        val response = authorService.createAuthorResponseDTO(authorsPartFailure)

        assertEquals(response.size, 2)

    }
}

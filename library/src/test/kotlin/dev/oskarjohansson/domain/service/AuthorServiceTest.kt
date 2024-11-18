package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.response.AuthorInBookResponseDTO
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.respository.AuthorRepository
import io.ktor.util.reflect.*
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.assertDoesNotThrow
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
    fun `Test that save does not throw error if author does not exist`() {
        every { authorRepository.findByAuthorName(any()) } returns null
        every { authorRepository.save(any()) } returns existingAuthor
        assertEquals(existingAuthor, authorService.saveAuthor(author))
    }


    @Test
    fun `test that getOrCreateAuthor saves a new author when author does not exist in the repository`() {
        every { authorRepository.findByAuthorName(any()) } returns null
        every { authorRepository.save(any()) } returns Author("123", "author2")

        assertDoesNotThrow { authorService.saveAuthor(author) }
    }

    @Test
    fun `test that createAuthorResponseDTO throws error if no Author is found`() {
        every { authorRepository.findById(any()) } returns Optional.empty()
        assertDoesNotThrow { authorService.createAuthorInBookResponseDTO(listOfTwoAuthors) }
    }


    @Test
    fun `test that createAuthorResponseDTO returns a AuthorResponseDTO when called with one author`() {
        val author = Author("1234", "AuthorSuccess")

        every { authorRepository.findById(any()) } returns Optional.of(author)

        val response = authorService.createAuthorInBookResponseDTO(listOf("1234"))

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

        assertDoesNotThrow { authorService.createAuthorInBookResponseDTO(listOfTwoAuthors) }
        assertTrue { authorService.createAuthorInBookResponseDTO(listOfTwoAuthors).size == 2 }

    }

    @Test
    fun `test that createAuthorResponseDTO returns a list of 2 object when called with a a list of 3 with 1 partial failure`() {
        val authorsPartFailure = listOf("1234", "Invalid", "2345")
        val author = Author("1234", "AuthorSuccess")
        val anotherAuthor = Author("2345", "AuthorSuccess")

        every { authorRepository.findById("1234") } returns Optional.of(author)
        every { authorRepository.findById("Invalid") } returns Optional.empty()
        every { authorRepository.findById("2345") } returns Optional.of(anotherAuthor)

        val response = authorService.createAuthorInBookResponseDTO(authorsPartFailure)

        assertEquals(response.size, 2)

    }

    @org.junit.jupiter.api.Test
    fun `test that getOrCreateAuthors create a new Author when no author is found in repository`() {
        val newAuthor = Author("New Author", "New Author")
        every { authorRepository.findByAuthorName(any()) } returns null
        every { authorRepository.save(any()) } returns newAuthor
        val authorsList =  authorService.getOrCreateAuthors(listOfTwoAuthors)


        assertEquals(2 ,authorsList.size)
        assertTrue(authorsList[0].instanceOf(Author::class))
    }

    @Test
    fun `test that getOrCreateAuthors create one new Author when one author is found in repository and returns one author from repository`() {

        val input = listOf("New Author","Existing Author")
        val newAuthor = Author("New Author", "New Author")
        val existingAuthor = Author("Existing Author", "Existing Author")


        every { authorRepository.findByAuthorName("New Author") } returns null
        every { authorRepository.findByAuthorName("Existing Author") } returns existingAuthor
        every { authorRepository.save(any()) } returns newAuthor
        val authorsList =  authorService.getOrCreateAuthors(input)



        assertEquals(newAuthor, authorsList[0])
        assertEquals(existingAuthor, authorsList[1])
        assertEquals(2 ,authorsList.size)
        assertTrue(authorsList[0].instanceOf(Author::class))
    }

}

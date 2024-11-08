package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.AuthorInBookResponseDTO
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.respository.AuthorRepository
import io.ktor.util.reflect.*
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import java.util.*
import kotlin.test.*

class AuthorServiceTest {
//
//    private val repository = mockk<AuthorRepository>()
//    private val dtoService = mock<LibraryService>()
//    private val authorService = AuthorService(repository, dtoService)
//    private val author = "Author"
//    private val existingAuthor = mockk<Author> {
//        every { authorName } returns author
//        every { authorId } returns "Author123"
//    }
//    private val listOfTwoAuthors = listOf("1234", "12354")
//
//    @Test
//    fun `Test that save throws error if author exist`() {
//        every { repository.findByAuthorName(any()) } returns existingAuthor
//        assertThrows<IllegalArgumentException> { authorService.saveAuthor(author) }
//    }
//
//    @Test
//    fun `Test that save does not throw error if author does not exist`() {
//        every { repository.findByAuthorName(any()) } returns null
//        every { repository.save(any()) } returns mockk<Author>()
//        assertDoesNotThrow { authorService.saveAuthor(author) }
//    }
//
//    // TODO: move to DtoServiceTest file ***
//    @Test
//    fun `that getOrCreateAuthor returns id when an author exist`() {
//        every { repository.findByAuthorName(any()) } returns existingAuthor
//        assertEquals(existingAuthor.authorId, dtoService.getOrCreateAuthor("author"))
//    }
//
//    @Test
//    fun `test that getOrCreateAuthor saves a new author when author does not exist in the repository`() {
//        every { repository.findByAuthorName(any()) } returns null
//        every { repository.save(any()) } returns Author("123", "author2", null)
//
//        assertEquals("123", dtoService.getOrCreateAuthor("NAME"))
//    }
//
//    @Test
//    fun `test that createAuthorResponseDTO throws error if no Author is found`() {
//        every { repository.findById(any()) } returns Optional.empty()
//
//        assertDoesNotThrow { dtoService.createAuthorResponseDTO(listOfTwoAuthors) }
//    }
//
//
//    @Test
//    fun `test that createAuthorResponseDTO returns a AuthorResponseDTO when called with one author`() {
//        val author = Author("1234", "AuthorSuccess")
//        val authorDto = AuthorInBookResponseDTO("1234", "AuthorSuccess")
//
//        every { repository.findById(any()) } returns Optional.of(author)
//
//        val response = dtoService.createAuthorResponseDTO(listOf("1234"))
//
//
//        println("expected: ${authorDto.authorName} ${authorDto.authorId}")
//        println("Actual: ${response.first().authorName} ${response.first().authorId}")
//
//        assertTrue { response[0].instanceOf(AuthorInBookResponseDTO::class) }
//        assertTrue { response.size == 1 }
//
//    }
//
//    @Test
//    fun `test that createAuthorResponseDTO returns a AuthorResponseDTO when called with many authors`() {
//        val author = Author("1234", "AuthorSuccess")
//        val authorDto = listOf(
//            AuthorInBookResponseDTO(
//                "1234", "AuthorSuccess"
//            ), AuthorInBookResponseDTO("23456", "AnotherSuccess")
//        )
//        every { repository.findById(any()) } returns Optional.of(author)
//
//        assertDoesNotThrow { dtoService.createAuthorResponseDTO(listOfTwoAuthors) }
//        assertTrue { dtoService.createAuthorResponseDTO(listOfTwoAuthors).size == 2 }
//
//    }
//
//    @Test
//    fun `test that createAuthorResponseDTO returns a list of 2 object when called with a a list of 3 with 1 partial failure`() {
//        val authorsPartFailure = listOf("1234", "Invalid", "2345")
//        val author = Author("1234", "AuthorSuccess")
//        val anotherAuthor = Author("2345", "AuthorSuccess")
//
//        every { repository.findById("1234") } returns Optional.of(author)
//        every { repository.findById("Invalid") } returns Optional.empty()
//        every { repository.findById("2345") } returns Optional.of(anotherAuthor)
//
//        val response = dtoService.createAuthorResponseDTO(authorsPartFailure)
//
//        assertEquals(response.size, 2)
//
//    }
//
//    // TODO: Move to DtoServiceTest file ^^^



}
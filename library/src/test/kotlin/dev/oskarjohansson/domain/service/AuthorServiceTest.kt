package dev.oskarjohansson.domain.service

import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.respository.AuthorRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthorServiceTest {

    private val repository = mockk<AuthorRepository>()
    private val service = AuthorService(repository)
    val author = "Author"
    val existingAuthor = mockk<Author>{
        every { authorName } returns author
        every { authorId } returns "Author123"
    }

    @Test
    fun `Test that save throws error if author exist`(){
        every { repository.findByAuthorName(any()) } returns existingAuthor
        assertThrows<IllegalArgumentException> { service.save(author) }
    }

    @Test
    fun `Test that save does not throw error if author does not exist`() {
        every {repository.findByAuthorName(any()) } returns null
        every { repository.save(any()) } returns mockk<Author>()
        assertDoesNotThrow { service.save(author) }
    }

    @Test
    fun `that getOrCreateAuthor returns id when an author exist`(){
        every { repository.findByAuthorName(any())} returns existingAuthor
        assertEquals(existingAuthor.authorId, service.getOrCreateAuthor("author") )
    }

    @Test
    fun `test that getOrCreateAuthor saves a new author when author does not exist in the repository`(){
        every { repository.findByAuthorName(any()) } returns null
        every { repository.save(any()) } returns Author("123", "author2", null)

        assertEquals("123", service.getOrCreateAuthor("NAME") )
    }

}
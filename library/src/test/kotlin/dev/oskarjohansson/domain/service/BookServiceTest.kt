package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.request.BookRequestDTO
import dev.oskarjohansson.domain.model.Book
import dev.oskarjohansson.respository.BookRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import kotlin.test.Test
import kotlin.test.assertEquals

class BookServiceTest {
    private val bookRepository = mockk<BookRepository>()
    private val bookService = BookService(bookRepository)
    private val mockBook = mockk<Book>()
    private val bookTitle = BookRequestDTO(title = "ABC")
    private val bookId = BookRequestDTO(bookId = "123")



    @Test
    fun `Test that findBookByIdOrTitle returns book by Id`(){
        every{ bookRepository.findByTitle(any()) } returns mockBook
        assertEquals(mockBook, bookService.findBookByIdOrTitle(bookTitle))
    }

    @Test
    fun `Test that findBookByIdOrTitle returns book by Title`(){
        every{ bookRepository.findByTitle(any()) } returns null
        every {bookRepository.findByBookId(any())} returns mockBook

        assertEquals(mockBook, bookService.findBookByIdOrTitle(bookId))
    }

    @Test
    fun `Test that findBookByIdOrTitle throws error when no book found`(){
        every{ bookRepository.findByTitle(any()) } returns null
        every {bookRepository.findByBookId(any())} returns null

        assertThrows<IllegalStateException> { bookService.findBookByIdOrTitle(bookId) }
    }

    @Test
    fun`Test that validateAuthorExistenceInBook throws error when authorId is persisted in book`(){
        every {bookRepository.findByBookId(any())?.authorIds?.contains(any()) } returns true
        assertThrows<IllegalArgumentException> { bookService.validateAuthorExistenceInBook("123", "ABD")  }
    }
}
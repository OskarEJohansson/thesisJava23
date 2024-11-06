package dev.oskarjohansson.api

import dev.oskarjohansson.api.dto.BookRequestDTO
import dev.oskarjohansson.api.dto.BookResponseDTO
import dev.oskarjohansson.domain.model.Book
import dev.oskarjohansson.domain.service.BookService
import dev.oskarjohansson.model.ResponseDTO

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/book")
class BookController(private val bookService: BookService) {


    @PostMapping("/v1/register-book")
    fun registerBook(@Valid @RequestBody book: BookRequestDTO): ResponseEntity<ResponseDTO<Book>> {

        return runCatching {
            ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDTO(status = HttpStatus.CREATED.value(), "Book saved", data = bookService.saveBook(book)))
        }.getOrElse {
            ResponseEntity.badRequest().body(
                ResponseDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    "Could not save book title: ${book.title} and author: ${book.authorName}, message: ${it.message}"
                )
            )
        }
    }

    @GetMapping("/v1/books")
    fun books(pageable: Pageable): ResponseEntity<ResponseDTO<Page<BookResponseDTO>>>{

        return runCatching {
            ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO(status = HttpStatus.OK.value(), message = "All books in the repository" , data =  bookService.getBooks(pageable))
        }.getOrThrow()
    }



}
package dev.oskarjohansson.api

import dev.oskarjohansson.api.dto.BookDTO
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.domain.model.Book
import dev.oskarjohansson.model.ResponseDTO
import dev.oskarjohansson.domain.service.AuthorService
import dev.oskarjohansson.domain.service.BookService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LibraryController(private val authorService: AuthorService, private val bookService: BookService) {


    @PostMapping
    fun registerAuthor(@Valid @RequestBody authorName: String): ResponseEntity<ResponseDTO<Author>> {

        return runCatching {
            ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDTO(HttpStatus.CREATED.value(), "Author created", authorService.save(authorName)))
        }.getOrElse {
            ResponseEntity.badRequest().body(
                ResponseDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    message = "Could not save author ${authorName}, ${it.message}"
                )
            )
        }
    }

    @PostMapping
    fun registerBook(@Valid @RequestBody book: BookDTO): ResponseEntity<ResponseDTO<Book>> {

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

    @PostMapping
    fun createReview
//
//    @PostMapping
//    fun registerReview():ResponseEntity<String>{
//        TODO("Add logic for registering a Review")
//    }
//
//    @GetMapping
//    fun getBooks():ResponseEntity<List<Book>>{
//        TODO("Add logic for retrieving all books")
//    }
//
//    @GetMapping
//    fun getAuthors():ResponseEntity<List<Author>>{
//        TODO("Add logic for retrieving all authors ")
//    }
//
//    @GetMapping
//    fun <T> getReviews(): ResponseEntity<T>{
//        TODO("""
//            Add logic for retrieving reviews
//            Decide how the reviews should be packed
//        """
//                )
//    }
//
//    @PutMapping
//    fun updateReview(){
//        TODO("Add logic for updating a Review object")
//    }
//
//    @PutMapping
//    fun updateBook(){
//        TODO("Add logic for updating a Book object")
//    }
//
//    @PutMapping
//    fun updateAuthor(){
//        TODO("Add logic for updating a Author object")
//    }
//
//
//
//
}
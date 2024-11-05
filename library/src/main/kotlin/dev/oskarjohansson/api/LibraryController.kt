package dev.oskarjohansson.api

import dev.oskarjohansson.api.dto.BookDTO
import dev.oskarjohansson.api.dto.ReviewDTO
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.domain.model.Book
import dev.oskarjohansson.domain.model.Review
import dev.oskarjohansson.model.ResponseDTO
import dev.oskarjohansson.domain.service.AuthorService
import dev.oskarjohansson.domain.service.BookService
import dev.oskarjohansson.domain.service.ReviewService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/library")
class LibraryController(
    private val authorService: AuthorService,
    private val bookService: BookService,
    private val reviewService: ReviewService
) {


    @PostMapping("/v1/register-author")
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

    @PostMapping("/v1/register-book")
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

    @PostMapping("/v1/create-review")
    fun createReview(
        @AuthenticationPrincipal jwt: Jwt,
        @Valid @RequestBody review: ReviewDTO
    ): ResponseEntity<ResponseDTO<Review>> {

        return runCatching {
            ResponseEntity.status(HttpStatus.CREATED)
                .body(
                    ResponseDTO(
                        HttpStatus.CREATED.value(),
                        "Review created",
                        reviewService.createReview(review, jwt)
                    )
                )

        }.getOrElse {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Could not save review"))
        }


    }

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
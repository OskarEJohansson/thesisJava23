package dev.oskarjohansson.api

import dev.oskarjohansson.api.dto.request.AddAuthorRequestDTO
import dev.oskarjohansson.api.dto.request.BookRequestDTO
import dev.oskarjohansson.api.dto.request.RegisterBookRequestDTO
import dev.oskarjohansson.api.dto.response.BookResponseDTO
import dev.oskarjohansson.domain.service.LibraryService
import dev.oskarjohansson.api.dto.ResponseDTO
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user/book")
class BookController(private val libraryService: LibraryService) {

    @PostMapping("/v1/register-book")
    fun registerBook(@Valid @RequestBody book: RegisterBookRequestDTO): ResponseEntity<ResponseDTO<BookResponseDTO>> {

        return runCatching {
            ResponseEntity.status(HttpStatus.CREATED)
                .body(
                    ResponseDTO(
                        HttpStatus.CREATED.value(),
                        "Book saved",
                        libraryService.saveBook(book)
                    )
                )
        }.getOrElse {
            ResponseEntity.badRequest().body(
                ResponseDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    "Could not save book title: ${book.title}, message: ${it.message}"
                )
            )
        }
    }

    @GetMapping("/v1/books")
    fun books(pageable: Pageable): ResponseEntity<ResponseDTO<Page<BookResponseDTO>>> {

        return runCatching {
            ResponseEntity.status(HttpStatus.OK)
                .body(
                    ResponseDTO(
                        HttpStatus.OK.value(),
                        "All books in the repository",
                        libraryService.getBooks(pageable)
                    )
                )
        }.getOrElse {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                    ResponseDTO(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Could not retrieve books from the repository: ${it.message}"
                    )
                )
        }
    }

    @GetMapping("/v1/get-book")
    fun getBook(@Valid @RequestBody request: BookRequestDTO): ResponseEntity<ResponseDTO<BookResponseDTO>>{

        return runCatching {
            ResponseEntity.status(HttpStatus.OK).body(ResponseDTO(HttpStatus.OK.value(),"Requested Book:", libraryService.getBookByIdOrTitle(request)))
        }.getOrElse {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Error while loading book: ${it.message}"))
        }
    }

    @PostMapping("/v1/add-author")
    fun addAuthor(@Valid @RequestBody authorRequest: AddAuthorRequestDTO ): ResponseEntity<ResponseDTO<BookResponseDTO>>{

        return runCatching {
            require(!authorRequest.authorName.isNullOrBlank()){
                "Author name must not be null"
            }
            ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO(HttpStatus.CREATED.value(), "Author added", libraryService.addAuthor(authorRequest)))
        }.getOrElse {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Could not register author, ${it.message} "))
        }
    }

}
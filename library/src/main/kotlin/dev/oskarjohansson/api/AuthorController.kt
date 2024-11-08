package dev.oskarjohansson.api

import dev.oskarjohansson.api.dto.AuthorResponseDTO
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.domain.service.AuthorService
import dev.oskarjohansson.domain.service.LibraryService
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
@RequestMapping("/author")
class AuthorController(private val libraryService: LibraryService) {

    @PostMapping("/v1/register-author")
    fun registerAuthor(@Valid @RequestBody authorName: String): ResponseEntity<ResponseDTO<Author>> {

        return runCatching {
            ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDTO(HttpStatus.CREATED.value(), "Author created", libraryService.saveAuthor(authorName)))
        }.getOrElse {
            ResponseEntity.badRequest().body(
                ResponseDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    message = "Could not save author ${authorName}, ${it.message}"
                )
            )
        }
    }


    // TODO: authors, a controller that sends all authors in a pageable object to the caller
    @GetMapping("/v1/authors")
    fun authors(pageable: Pageable): ResponseEntity<ResponseDTO<Page<AuthorResponseDTO>>> {

        return runCatching {
            ResponseEntity.status(HttpStatus.OK).body(
                ResponseDTO(
                    HttpStatus.OK.value(),
                    "All authors in the database",
                    libraryService.getAuthors(pageable)
                )
            )
        }.getOrElse {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ResponseDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error while retrieving authors, ${it.message}"
                )
            )
        }
    }
}
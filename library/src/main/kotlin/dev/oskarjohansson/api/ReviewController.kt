package dev.oskarjohansson.api

import dev.oskarjohansson.api.dto.ReviewDTO
import dev.oskarjohansson.api.dto.ReviewResponseDTO
import dev.oskarjohansson.domain.model.Review
import dev.oskarjohansson.domain.service.LibraryService
import dev.oskarjohansson.model.ResponseDTO
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/review")
class ReviewController(private val libraryService: LibraryService) {

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
                        libraryService.createReview(review, jwt)
                    )
                )

        }.getOrElse {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Could not save review: ${it.message}"))
        }


    }

    // TODO: VALIDATE BOOKID
    @GetMapping("/v1/get-reviews")
    fun getReviews(
        pageable: Pageable,
        @RequestBody bookId: String
    ): ResponseEntity<ResponseDTO<Page<ReviewResponseDTO>>> {
        return runCatching {

            ResponseEntity.status(HttpStatus.OK).body(
                ResponseDTO(
                    HttpStatus.OK.value(),
                    "All reviews for book: $bookId",
                    libraryService.getReviews(pageable, bookId)
                )
            )

        }.getOrElse {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    "Could not load reviews for book: ${bookId}, message:${it.message}"
                )
            )
        }
    }
}
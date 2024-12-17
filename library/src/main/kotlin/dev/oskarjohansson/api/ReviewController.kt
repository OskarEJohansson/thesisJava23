package dev.oskarjohansson.api

import dev.oskarjohansson.api.dto.request.ReviewRequestDTO
import dev.oskarjohansson.api.dto.response.ReviewResponseDTO
import dev.oskarjohansson.domain.model.Review
import dev.oskarjohansson.domain.service.LibraryService
import dev.oskarjohansson.api.dto.ResponseDTO
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user/review")
class ReviewController(private val libraryService: LibraryService) {

    @PostMapping("/v1/create-review")
    fun createReview(
        @AuthenticationPrincipal jwt: Jwt,
        @Valid @RequestBody review: ReviewRequestDTO
    ): ResponseEntity<ResponseDTO<Review>> {
        return runCatching {

            require(!review.bookId.isNullOrBlank()) {
                "BookId must not be null"
            }

            ResponseEntity.status(HttpStatus.CREATED)
                .body(
                    ResponseDTO(
                        HttpStatus.CREATED.value(),
                        "Review created",
                        libraryService.saveReview(review, jwt)
                    )
                )
        }.getOrElse {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Could not save review: ${it.message}"))
        }
    }

    @GetMapping("/v1/get-reviews")
    fun getReviews(
        pageable: Pageable,
        @RequestBody request: ReviewRequestDTO
    ): ResponseEntity<ResponseDTO<Page<ReviewResponseDTO>>> {
        return runCatching {

            require(!request.bookId.isNullOrBlank()) {
                "BookId must not be null"
            }

            ResponseEntity.status(HttpStatus.OK).body(
                ResponseDTO(
                    HttpStatus.OK.value(),
                    "All reviews for book:  ${libraryService.getBookById(request.bookId!!).title}",
                    libraryService.getReviews(pageable, request.bookId!!)
                )
            )

        }.getOrElse {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    "Could not load reviews for book: ${request}, message:${it.message}"
                )
            )
        }
    }

    @PutMapping("/v1/update-review")
    fun updateReview(
        @AuthenticationPrincipal jwt: Jwt,
        @RequestBody request: ReviewRequestDTO
    ): ResponseEntity<ResponseDTO<ReviewResponseDTO>> {
        return runCatching {

            require(!request.reviewId.isNullOrBlank()) {
                "RequestId must no be null"
            }

            ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO(HttpStatus.OK.value(), "Review updated", libraryService.updateReview(jwt, request)))

        }.getOrElse {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Could not update review: ${it.message}"))
        }
    }

    @DeleteMapping("/v1/delete-review")
    fun deleteReview(
        @AuthenticationPrincipal jwt: Jwt,
        @Valid @RequestBody request: ReviewRequestDTO
    ): ResponseEntity<ResponseDTO<Unit>> {
        return runCatching {

        require(!request.reviewId.isNullOrBlank()) {
            "ReviewId must not be null"}

            ResponseEntity.status(HttpStatus.OK).body(
                ResponseDTO(
                    HttpStatus.OK.value(),
                    "Review deleted successfully",
                    libraryService.deleteReview(jwt, request.reviewId!!)
                )
            )
        }.getOrElse {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    ResponseDTO(
                        HttpStatus.BAD_REQUEST.value(),
                        "Error deleting review with id ${request.reviewId}, message: ${it.message}",
                    )
                )
        }
    }
}
package dev.oskarjohansson.api

import dev.oskarjohansson.api.dto.ReviewDTO
import dev.oskarjohansson.domain.model.Review
import dev.oskarjohansson.domain.service.ReviewService
import dev.oskarjohansson.model.ResponseDTO
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
@RequestMapping("/review")
class ReviewController(private val reviewService: ReviewService) {

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
}
package dev.oskarjohansson.domain.service

import dev.oskarjohansson.api.dto.ReviewDTO
import dev.oskarjohansson.domain.model.Review
import io.ktor.http.*
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import java.lang.IllegalStateException
import java.security.Principal


@Service
class ReviewService(private val reviewService: ReviewService, private val bookService: BookService) {


    // TODO: Read out the userId from the JWT claim from @AuthenticationPrincipal and
    fun createReview(review: ReviewDTO, jwt:Jwt): Review? {

        // TODO: Check that userID exist
       val userId = jwt.claims["userId"].toString()

        //TODO: Check that bookID exist
        if(bookService.findBookById(review.bookId)) {


            // TODO: Save review or throw exception
        }

        return null

    }
}
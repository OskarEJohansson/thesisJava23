package dev.oskarjohansson.api

import dev.oskarjohansson.domain.service.AuthorService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LibraryController(private val authorService: AuthorService) {


    @PostMapping
    fun registerBook(@Valid @RequestBody authorName: String): ResponseEntity<String> {

        return runCatching {
            ResponseEntity.ok().body(authorService.save(authorName).toString())
        }.getOrElse {
            ResponseEntity.badRequest().body("Could not save author ${authorName}, ${it.message}")
        }


    }
//
//    @PostMapping
//    fun registerAuthor():ResponseEntity<String>{
//        TODO("Add logic for registering an Author")
//    }
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
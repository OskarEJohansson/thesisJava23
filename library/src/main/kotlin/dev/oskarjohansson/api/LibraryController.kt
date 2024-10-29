package dev.oskarjohansson.api

import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.domain.model.Book
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LibraryController


//    @PostMapping
//    fun registerBook():ResponseEntity<String>{
//        TODO("Add logic for registering a book")
//    }
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
//}
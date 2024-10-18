package dev.oskarjohansson.domain.model

import dev.oskarjohansson.domain.enums.Role
import org.springframework.data.mongodb.core.mapping.Document

@Document(collation = "Users")
data class User(val id:String,
                val email:String,
                val username: String,
                val password:String,
                val role: Role,
                val createdAt:String,
                val library:List<LibraryEntry>) {

    data class LibraryEntry(
        val bookid:String,
        val reviewId:String)

}
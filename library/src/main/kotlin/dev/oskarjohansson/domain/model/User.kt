package dev.oskarjohansson.domain.model

import dev.oskarjohansson.domain.enums.Role
import jakarta.validation.constraints.Email
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.time.LocalDateTime

@Document(collation = "Users")
data class User(val id:String?,
                val email: Email,
                val username: String,
                val password:String,
                val role: Role,
                val createdAt: LocalDateTime,
                val library:List<LibraryEntry>?) {

    data class LibraryEntry(
        val bookid:String,
        val reviewId:String)

}
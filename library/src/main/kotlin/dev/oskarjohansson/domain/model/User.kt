package dev.oskarjohansson.domain.model

import jakarta.validation.constraints.Email
import org.springframework.data.mongodb.core.aggregation.VariableOperators
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "Users")
data class User(
    val id: String?,
    val email: String,
    val username: String,
    val password: String,
    val role: Role,
    val createdAt: LocalDateTime,
    val library: List<LibraryEntry>?
) {
    data class LibraryEntry(
        val bookId: String,
        val reviewId: String
    )

    enum class Role {
        ROLE_USER,
        ROLE_ADMIN
    }
}


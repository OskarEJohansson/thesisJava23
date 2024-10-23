package dev.oskarjohansson.domain.entity

import com.mongodb.internal.connection.Time
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.apache.tomcat.jni.Library
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId

@Document(collation = "Users")
data class User(
    @MongoId
    val id: String,
    val email: String,
    @NotBlank(message = "Username must not be empty") @Size(
        min = 4,
        max = 20,
        message = "Username must be between 4 and 20 characters"
    ) @Indexed(unique = true)
    val username: String,
    @NotBlank(message = "Password must not be blank") @Size(
        min = 6,
        max = 30,
        message = "Password must be between 6 and 30 characters"
    )
    val password: String,
    val role: Role,
    val dateJoined: Time,
    val library: Library
)
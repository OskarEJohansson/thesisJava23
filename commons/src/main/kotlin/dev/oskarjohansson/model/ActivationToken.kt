package dev.oskarjohansson.model

import org.springframework.data.mongodb.core.mapping.Document
import java.time.Duration
import java.time.Instant
import java.util.*

@Document(collection = "ActivationTokens")
data class ActivationToken(
    val id:String? = null,
    val email: String,
    val token: String = UUID.randomUUID().toString(),
    val timeCreated: Instant = Instant.now(),
    val expirationDate: Instant = Instant.now().plus(Duration.ofHours(1))
)

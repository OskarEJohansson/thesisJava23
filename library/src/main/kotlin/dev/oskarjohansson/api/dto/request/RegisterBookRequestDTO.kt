package dev.oskarjohansson.api.dto.request

import dev.oskarjohansson.domain.enums.Genres
import org.springframework.validation.annotation.Validated

@Validated
data class RegisterBookRequestDTO(
    val title: String,
    val authors: List<String>,
    val genre: Genres
)


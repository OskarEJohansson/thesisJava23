package dev.oskarjohansson.api.dto.request

import org.springframework.validation.annotation.Validated

@Validated
data class AuthorRequestDTO(val authorName: String)

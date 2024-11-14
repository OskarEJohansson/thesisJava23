package dev.oskarjohansson.api.dto

import org.springframework.validation.annotation.Validated

@Validated
data class AuthorRequestDTO(val authorName: String)

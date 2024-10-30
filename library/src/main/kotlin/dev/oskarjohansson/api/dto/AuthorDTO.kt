package dev.oskarjohansson.api.dto

import org.springframework.validation.annotation.Validated

@Validated
data class AuthorDTO(private val authorName: String)

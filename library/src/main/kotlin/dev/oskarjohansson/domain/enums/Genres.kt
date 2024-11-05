package dev.oskarjohansson.domain.enums

import org.springframework.validation.annotation.Validated

@Validated
enum class Genres{
    FICTION,
    NON_FICTION,
    FANTASY,
    MYSTERY,
    ROMANCE,
    SCIENCE_FICTION,
    THRILLER,
    HORROR,
    HISTORICAL,
    BIOGRAPHY
}
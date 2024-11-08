package dev.oskarjohansson.domain.service


import dev.oskarjohansson.api.dto.AuthorResponseDTO
import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.respository.AuthorRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class AuthorService(private val authorRepository: AuthorRepository, private val dtoService: DtoService) {

    fun save(authorName: String): Author {
        authorRepository.findByAuthorName(authorName)?.let {
            throw IllegalArgumentException("Author already exist, ${it.authorName}, id: ${it.authorId} ")
        }

        return authorRepository.save(Author(authorName = authorName))
    }


    fun createPageableAuthor(pageable: Pageable): Page<AuthorResponseDTO> {
        return authorRepository.findAll(pageable)
            .map { author ->
                author.authorId?.let { authorId ->
                    author.toAuthorResponseDTO(dtoService.createBookInAuthorResponseDTO(authorId))
                }
            }
    }

}


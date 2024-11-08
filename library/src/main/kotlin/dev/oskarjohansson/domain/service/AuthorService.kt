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



//    fun createAuthorResponseDTO(listOfAuthorIds: List<String>): List<AuthorInBookResponseDTO> {
//
//        return listOfAuthorIds.mapNotNull { authorId ->
//            authorRepository.findById(authorId).getOrNull()?.let {
//                AuthorInBookResponseDTO(it.authorId!!, it.authorName)
//            }
//        }
//    }

    fun createPageableAuthor(pageable: Pageable): Page<AuthorResponseDTO> {
        return authorRepository.findAll(pageable).map { author ->
            takeIf {author.authorId != null}
            author.toAuthorResponseDTO(
                dtoService.createBookInAuthorResponseDTO(author.publishedBooksId)
            )

        }

    }

}


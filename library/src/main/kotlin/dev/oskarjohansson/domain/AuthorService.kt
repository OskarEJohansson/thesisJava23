package dev.oskarjohansson.domain


import dev.oskarjohansson.domain.model.Author
import dev.oskarjohansson.respository.AuthorRepository
import org.springframework.stereotype.Service


@Service
class AuthorService(private val authorRepository: AuthorRepository) {

    fun save(authorName: String): Author {
        authorRepository.findByAuthorName(authorName)?.let {
            throw IllegalStateException("Author already exist, ${it.authorName}, id: ${it.authorId} ")
        }

        return authorRepository.save(Author(authorName = authorName))
    }

    fun getOrCreateAuthor(authorName: String): String?{
        return authorRepository.findByAuthorName(authorName)?.authorId ?: authorRepository.save(Author(authorName = authorName)).authorId
    }


}

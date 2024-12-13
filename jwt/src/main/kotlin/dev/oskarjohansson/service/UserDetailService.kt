package dev.oskarjohansson.service

import dev.oskarjohansson.model.CustomUserDetails
import dev.oskarjohansson.model.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class UserDetailService(private val repositoryService: RepositoryService) : UserDetailsService {

    private val LOG: Logger = LoggerFactory.getLogger(UserDetailService::class.java)

    override fun loadUserByUsername(username: String): UserDetails? {
        return runCatching {
           repositoryService.getUserByUsername(username)
               .takeIf { user -> user.isEnabled }
               ?.let {user ->
               createUserDetailsAndGrantAuthority(user)
            } ?: throw IllegalStateException("User not activated")
        }.getOrElse {
            LOG.debug("Failed to load user by username: $username")
            throw UsernameNotFoundException("Username not found")
        }
    }

    fun createUserDetailsAndGrantAuthority(user: User): UserDetails =
        user.id?.let {
            CustomUserDetails(user.username, user.password, listOf(SimpleGrantedAuthority(user.role.authority)), it)
        } ?: throw IllegalStateException("User ID is required to create a User")
}


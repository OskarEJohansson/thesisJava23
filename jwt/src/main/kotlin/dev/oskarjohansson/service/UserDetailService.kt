package dev.oskarjohansson.service

import dev.oskarjohansson.model.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.User as SecurityCoreUser


@Service
class UserDetailService(private val repositoryService: RepositoryService) : UserDetailsService {

    private val LOG: Logger = LoggerFactory.getLogger(UserDetailService::class.java)

    override fun loadUserByUsername(username: String): UserDetails {
        return runCatching {

            LOG.debug("Attempting to load user by username: $username")
            val user = repositoryService.getUserByUsername(username)
            createUserDetailsAndGrantAuthority(
                user
            )
        }.getOrElse {
            LOG.debug("Failed to load user by username: $username")
            throw UsernameNotFoundException("Username not found")}
    }

    fun createUserDetailsAndGrantAuthority(user: User): UserDetails =
        SecurityCoreUser.builder()
            .username(user.username)
            .password(user.password)
            .authorities(user.role.authority)
            .build()
}


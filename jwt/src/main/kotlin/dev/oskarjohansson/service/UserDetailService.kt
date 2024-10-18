package dev.oskarjohansson.service

import dev.oskarjohansson.domain.entity.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.User as SecurityCoreUser


@Service
class UserDetailService(private val repositoryService: RepositoryService) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
        repositoryService.getUserByUsername(username)
            ?.let { createUserDetailsAndGrantAuthority(it) }
            ?: throw UsernameNotFoundException("Username not found")
}

fun createUserDetailsAndGrantAuthority(user: User): UserDetails =
    SecurityCoreUser.builder()
        .username(user.username)
        .password(user.password)
        .authorities(user.role.authority)
        .build()
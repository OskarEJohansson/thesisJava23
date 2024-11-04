package dev.oskarjohansson.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


data class CustomUserDetails(private val username: String, private val password: String, private val authorities: Collection<GrantedAuthority>, private val userId: String): UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities.toMutableList()

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    fun getUserId(): String = userId

}
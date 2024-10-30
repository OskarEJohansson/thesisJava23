package dev.oskarjohansson.model

import org.springframework.security.core.GrantedAuthority

enum class Role(
    private val authority: String
) : GrantedAuthority {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    override fun getAuthority(): String {
        return authority
    }

}
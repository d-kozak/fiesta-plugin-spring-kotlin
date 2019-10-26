package cz.esn.fiesta.fiestapluginspringkotlin.config

import cz.esn.fiesta.fiestapluginspringkotlin.dto.FiestaUser
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication

class FiestaDelegatingAuthProvider : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication? {
        val email = authentication.name
        val password = authentication.credentials.toString()

        return UsernamePasswordAuthenticationToken(
                FiestaUser(email, "token"),
                password,
                mutableListOf()
        )
    }

    override fun supports(authentication: Class<*>?): Boolean = authentication is UsernamePasswordAuthenticationToken
}
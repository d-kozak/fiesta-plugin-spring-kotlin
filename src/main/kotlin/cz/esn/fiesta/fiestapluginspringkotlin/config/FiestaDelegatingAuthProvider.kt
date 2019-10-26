package cz.esn.fiesta.fiestapluginspringkotlin.config

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import cz.esn.fiesta.fiestapluginspringkotlin.dto.FiestaUser
import cz.esn.fiesta.fiestapluginspringkotlin.utils.toDto
import cz.esn.fiesta.fiestapluginspringkotlin.utils.toJsonNode
import kotlinx.coroutines.runBlocking
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component


@Component
class FiestaDelegatingAuthProvider : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication? {
        val email = authentication.name
        val password = authentication.credentials.toString()
        return try {
            val user = runBlocking {
                val token = Fuel.upload("https://fiesta.esncz.org/api/auth", parameters = listOf("email" to email, "password" to password))
                        .awaitString()
                        .toJsonNode()["access_token"]
                        ?.textValue() ?: throw IllegalArgumentException("could not extract access token")

                val user = Fuel.get("https://fiesta.esncz.org/api/profile")
                        .header("X-Auth-Token", token)
                        .awaitString()
                        .toDto<FiestaUser>()
                user.token = token
                user
            }
            UsernamePasswordAuthenticationToken(
                    user,
                    password,
                    mutableListOf()
            )
        } catch (ex: Exception) {
            throw BadCredentialsException(ex.message)
        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication.equals(UsernamePasswordAuthenticationToken::class.java)
    }
}
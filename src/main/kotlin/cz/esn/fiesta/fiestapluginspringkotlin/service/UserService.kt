package cz.esn.fiesta.fiestapluginspringkotlin.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserService {

    fun getUser() = SecurityContextHolder.getContext().authentication.principal
}
package cz.esn.fiesta.fiestapluginspringkotlin.controller

import cz.esn.fiesta.fiestapluginspringkotlin.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService) {


    @GetMapping
    fun getDetails() = userService.getUser()
}
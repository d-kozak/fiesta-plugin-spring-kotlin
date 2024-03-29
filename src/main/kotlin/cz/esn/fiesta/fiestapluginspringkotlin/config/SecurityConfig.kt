package cz.esn.fiesta.fiestapluginspringkotlin.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Configuration
class SecurityConfig(
        val authProvider: FiestaDelegatingAuthProvider
) : WebSecurityConfigurerAdapter() {



    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
                .cors()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers("/api/login")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/api/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler())
                .and()
                .logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler(HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authProvider)
    }

    @Bean
    fun authenticationEntryPoint() = AuthenticationEntryPoint { _, response, _ ->
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized")
    }

    @Bean
    fun authenticationSuccessHandler() = object : SimpleUrlAuthenticationSuccessHandler() {
        override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse?, authentication: Authentication) {
            clearAuthenticationAttributes(request)
            redirectStrategy.sendRedirect(request, response, "/api/user")
        }
    }

    @Bean
    fun authenticationFailureHandler() = SimpleUrlAuthenticationFailureHandler()
}

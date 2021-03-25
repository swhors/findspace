package com.simpson.findspace.domain.config.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import kotlin.jvm.Throws


@EnableWebSecurity
class SecurityConfig(
    @Autowired private val jwtTokenProvider: JwtTokenProvider
) : WebSecurityConfigurerAdapter(){
    override fun configure(http: HttpSecurity?) {
        http {
            httpBasic { disable() }
            csrf { disable() }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            authorizeRequests {
                authorize("/admin/**", hasRole("ADMIN"))
                authorize("/user/**", hasRole("USER"))
                authorize("/login", permitAll)
                authorize("/**", authenticated)
            }
        }
        http?.requestMatchers()?.antMatchers("/api/search/**", "/join")?.and()?.
            addFilterBefore(JwtAuthenticationFilter(jwtTokenProvider),
                            UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }
}
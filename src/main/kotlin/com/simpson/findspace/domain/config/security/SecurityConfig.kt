//https://kjkjjang.wordpress.com/category/%EA%B0%9C%EB%B0%9C/spring/

package com.simpson.findspace.domain.config.security

import com.simpson.findspace.domain.service.h2.SearchHistorySvc
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
    @Autowired private val jwtTokenProvider: JwtTokenProvider,
    @Autowired private val historySvc: SearchHistorySvc
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
//                authorize("/hello", permitAll)
//                authorize("/user/login", permitAll)
//                authorize(HttpMethod.OPTIONS, "/**", permitAll)
//                authorize("/api/v1/search", hasAnyRole(AccountRole.USER.toString()))
//                authorize("/**", authenticated)
//                authorize( anyRequest, permitAll)
            }
        }
        http?.antMatcher("/api/search/**")?.
            addFilterBefore(JwtAuthenticationFilter(jwtTokenProvider, historySvc),
                            UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }
}
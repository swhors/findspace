package com.simpson.findspace.domain.config.security

import com.simpson.findspace.domain.service.h2.SearchHistorySvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import kotlin.Throws


class JwtAuthenticationFilter(@Autowired val jwtTokenProvider: JwtTokenProvider,
                              @Autowired val searchHistorySvc: SearchHistorySvc)
    : GenericFilterBean(){

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest?,
                          response: ServletResponse?,
                          chain: FilterChain?) {
        // Get jwt token from header.
        val token: String = jwtTokenProvider.resolveToken(request as HttpServletRequest)!!
        // validate jwt token
        if (jwtTokenProvider.validateToken(token)) {
            val prefix = "/api/search/place/"
            if (request.requestURI.contains(prefix)) {
                val keyword = request.requestURI.substring(prefix.length)
                val userName = jwtTokenProvider.getUserPk(token)
                userName?.let { searchHistorySvc.saveUserHistory(it, keyword) }
            }
            // Get user information
            val authentication: Authentication = jwtTokenProvider.getAuthentication(token) !!
            // store Authentication object to SecurityContext
            SecurityContextHolder.getContext().authentication = authentication
        }
        chain!!.doFilter(request, response)
    }
}


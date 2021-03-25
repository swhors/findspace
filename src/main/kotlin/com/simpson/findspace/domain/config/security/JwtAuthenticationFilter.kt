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

/***
 * 이 코드는 다음 사이트의 자바 코드를 참조하여 작성하였습니다.
 * - https://daddyprogrammer.org/post/636/springboot2-springsecurity-authentication-authorization/
 *
 * 2021/03/25 swhors@naver.com
 */
class JwtAuthenticationFilter(@Autowired val jwtTokenProvider: JwtTokenProvider)
    : GenericFilterBean(){

    /***
     * 설명 :
     *  이 함수는 SecurityFilter에서 JWT의 값을 파싱하여 유효성을 판단하는
     *  코드 입니다.
     */
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest?,
                          response: ServletResponse?,
                          chain: FilterChain?) {
        // Get jwt token from header.
        val token: String = jwtTokenProvider.resolveToken(request as HttpServletRequest)!!
        // JWT 토큰 유효성 검가
        if (jwtTokenProvider.validateToken(token)) {
            // Get user information
            val authentication: Authentication = jwtTokenProvider.getAuthentication(token) !!
            // store Authentication object to SecurityContext
            SecurityContextHolder.getContext().authentication = authentication
        }
        chain!!.doFilter(request, response)
    }
}


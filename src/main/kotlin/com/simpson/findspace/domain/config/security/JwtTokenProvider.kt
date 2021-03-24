package com.simpson.findspace.domain.config.security

import com.simpson.findspace.domain.service.h2.AccountSvc
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest
import java.util.Base64
import java.util.Date

/***
 * 이 코드는 다음 사이트의 자바 코드를 참조하여 성한 것입니다.
 * - https://daddyprogrammer.org/post/636/springboot2-springsecurity-authentication-authorization/
 *
 * 2021/03/25 swhors@naver.com
 */
@RequiredArgsConstructor
@Component
class JwtTokenProvider(@Autowired private val accountSvc: AccountSvc,
                       @Autowired private val jwtProperty: JwtProperty) {

    private var secretKey = "findplace"

    // 객체 초기화, "secretKey"를 Base64로 인코딩한다.
    @PostConstruct
    protected fun init() {
        secretKey = Base64.getEncoder().encodeToString(jwtProperty.secretkey().toByteArray())
    }

    // JWT 토큰 생성
    fun createToken(userPk: String?, roles: String): String? {
        val claims: Claims = Jwts.claims().setSubject(userPk) // JWT payload 에 저장되는 정보단위
        claims.put("roles", roles) // 정보는 key / value 쌍으로 저장된다.
        val now = Date()
        return Jwts.builder()
            .setClaims(claims) // 정보 저장
            .setIssuedAt(now) // 토큰 발행 시간 정보
//            .setExpiration(Date(now.time + tokenValidTime)) // set Expire Time
            .setExpiration(Date(now.time + jwtProperty.duration().toLong())) // set Expire Time
            .signWith(SignatureAlgorithm.HS256, secretKey) // 사용할 암호화 알고리즘과
            // signature 에 들어갈 secret값 세팅
            .compact()
    }

    // JWT 토큰에서 인증 정보 조회
    fun getAuthentication(token: String?): Authentication? {
        val userDetails = accountSvc.loadUserByUsername(getUserPk(token)!!)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    // 토큰에서 회원 정보 추출
    fun getUserPk(token: String?): String? {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
    }

    // Request의 Header에서 token 값을 가져옵니다. "X-AUTH-TOKEN" : "TOKEN값'
    fun resolveToken(request: HttpServletRequest): String? {
        return request.getHeader("X-AUTH-TOKEN")
    }

    // 토큰의 유효성 + 만료일자 확인
    fun validateToken(jwtToken: String?): Boolean {
        return try {
            val claims: Jws<Claims> = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken)
            !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }
}
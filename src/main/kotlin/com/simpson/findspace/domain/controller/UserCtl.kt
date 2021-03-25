package com.simpson.findspace.domain.controller

import com.google.gson.Gson
import com.simpson.findspace.domain.model.h2.Account
import com.simpson.findspace.domain.config.security.JwtTokenProvider
import com.simpson.findspace.domain.model.local.Error
import com.simpson.findspace.domain.model.local.JoinResult
import com.simpson.findspace.domain.model.local.LoginResult
import com.simpson.findspace.domain.service.h2.AccountSvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.lang.Exception
import javax.annotation.security.RolesAllowed

@RestController
class UserCtl(@Autowired private val passwordEncoder: PasswordEncoder,
              @Autowired private val jwtTokenProvider: JwtTokenProvider,
              @Autowired private val accountSvc: AccountSvc) {

    private val adminName = "admin@test.com"

    @GetMapping("/hello")
    fun printHello() : String {
        print("hello")
        return "hello"
    }

    fun joinInternal(userName: String, password: String) : String {
        return try {
            val id = accountSvc.saveAccount(userName, password).id
            Gson().toJson(id?.let { JoinResult(it) })
        } catch (e: Exception) {
            Gson().toJson(Error(500, e.message.toString()))
        }
    }

    /***
     * 설명 :
     *  회원 가입
     *
     * Param:
     *   user - 사용자의 ID와 패스워드를 갖는 HashMap
     * Return
     *   String = 사용자 ID를 갖는 Json 데이터
     */
    @PostMapping("/join")
    @RolesAllowed("ROLE_ADMIN")
    fun join(@RequestBody user: Map<String?, String?>) : String {
        return if (SecurityContextHolder.getContext().authentication.name == adminName) {
            joinInternal(user["userName"] !!, user["password"] !!)
        } else {
            Gson().toJson(Error(500, "관리자 권한이 없습니다."))
        }
    }
    
    /***
     * 설명 :
     *  로그인
     *
     * Param:
     *   user - 사용자의 ID와 패스워드를 갖는 HashMap
     * Return
     *   String = JWT_Token을 갖는 Json 데이터
     */
    @PostMapping("/login")
    fun login(@RequestBody user: Map<String?, String?>): String? {
        val account: Account = user["userName"]?.let { accountSvc.findByUserName(it) }
                               ?: return Gson().toJson(Error(500, "가입 되지 않은 사용자 입니다."))
        return if ( passwordEncoder.matches(user["password"], account.password)) {
            val token = jwtTokenProvider.createToken(account.userName, account.roles !!.stream().toString())
            Gson().toJson(token?.let { LoginResult.Builder().id(account.id).token(it).build() })
        } else {
            Gson().toJson(Error(500, "잘못 된 비밀 번호 입니다."))
        }
    }
}
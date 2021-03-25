package com.simpson.findspace.domain.controller

import com.google.gson.Gson
import com.simpson.findspace.domain.model.h2.Account
import com.simpson.findspace.domain.model.h2.AccountRole
import com.simpson.findspace.domain.repository.AccountRepo
import com.simpson.findspace.domain.config.security.JwtTokenProvider
import com.simpson.findspace.domain.model.local.JoinResult
import com.simpson.findspace.domain.model.local.LoginResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
class UserCtl(@Autowired private val passwordEncoder: PasswordEncoder,
              @Autowired private val jwtTokenProvider: JwtTokenProvider,
              @Autowired private val accountRepo: AccountRepo) {

    @GetMapping("/hello")
    fun printHello() : String {
        print("hello")
        return "hello"
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
    fun join(@RequestBody user: Map<String?, String?>): String? {
        val id = accountRepo.save(
                Account.Builder()
                        .userName(user["userName"])
                        .password(passwordEncoder.encode(user["password"]))
                        .roles(AccountRole.USER)
                        .build()).id
        return Gson().toJson(id?.let { JoinResult(it) })
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
        val account: Account = user["userName"]?.let { accountRepo.findByUserName(it) }
                               ?: throw java.lang.IllegalArgumentException("가입 되지 않은 사용자 입니다.")
        require(passwordEncoder.matches(user["password"], account.password)) {"잘못 된 비밀번호 입니다."}
        val token = jwtTokenProvider.createToken(account.userName, account.roles!!.stream().toString())
        return Gson().toJson(token?.let { LoginResult.Builder().id(account.id).token(it).build() })
    }
}
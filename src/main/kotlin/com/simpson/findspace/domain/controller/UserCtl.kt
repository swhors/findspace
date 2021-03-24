package com.simpson.findspace.domain.controller

import com.simpson.findspace.domain.model.h2.Account
import com.simpson.findspace.domain.model.h2.AccountRole
import com.simpson.findspace.domain.repository.AccountRepo
import com.simpson.findspace.domain.config.security.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
class UserCtl(@Autowired private val passwordEncoder: PasswordEncoder,
              @Autowired private val jwtTokenProvider: JwtTokenProvider,
              @Autowired private val accountRepo: AccountRepo) {

    @GetMapping("/hello")
    fun printHello() : String {
        print("hello")
        return "hello"
    }

    // 회원가입
    @PostMapping("/join")
    fun join(@RequestBody user: Map<String?, String?>): Long? {
        return this.accountRepo.save(Account.Builder().
                    userName(user["userName"]).
                    password(passwordEncoder.encode(user["password"])).
                    roles(AccountRole.USER).build()).id
    }

    // 로그인
    @PostMapping("/login")
    fun login(@RequestBody user: Map<String?, String?>): String? {
        val account: Account = user["userName"]?.let { accountRepo.findByUserName(it) }
                               ?: throw java.lang.IllegalArgumentException("가입 되지 않은 사용자 입니다.")
        val userName = user["userName"]
        val password = user["password"]
        print("login user=$userName, password=$password")
        require(passwordEncoder.matches(user["password"], account.password)) {"잘못 된 비밀번호 입니다."}
        return jwtTokenProvider.createToken(account.userName, account.roles!!.stream().toString())
    }
}
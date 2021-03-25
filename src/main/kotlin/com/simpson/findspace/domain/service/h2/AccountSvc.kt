package com.simpson.findspace.domain.service.h2

import com.simpson.findspace.domain.model.h2.Account
import com.simpson.findspace.domain.model.h2.AccountRole
import com.simpson.findspace.domain.repository.AccountRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AccountSvc(@Autowired private val accountRepo: AccountRepo,
                 @Autowired private val passwordEncoder: PasswordEncoder) : UserDetailsService {
    override fun loadUserByUsername(userName: String): UserDetails {
        return accountRepo.findByUserName(userName)?.getAuthorities() ?:
               throw UsernameNotFoundException("$userName can not found")
    }

    /**
     *  설명 :
     *    사용자의 계정을 저장합니다.
     *    사용자 계정이 있으면 암호를 업데이트 합니다.
     *  Param :
     *    userName : String, 사용자 ID
     *    password : String, 사용자 암호
     *  Return :
     *    Account : 저장 된 값
     */
    fun saveAccount(userName: String, password: String): Account {
        val storedAccount = accountRepo.findByUserName(userName)
                            ?: return accountRepo.save(Account.Builder()
                                .userName(userName)
                                .roles(AccountRole.USER)
                                .password(passwordEncoder.encode(password))
                                .build())
        storedAccount.password = passwordEncoder.encode(passwordEncoder.encode(password))
        return accountRepo.save(storedAccount)
    }

    fun findByUserName(userName: String) : Account? {
        return accountRepo.findByUserName(userName)
    }
}
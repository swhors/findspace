package com.simpson.findspace.domain.service.h2

import com.simpson.findspace.domain.model.h2.Account
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

    fun saveAccount(account: Account) {
        account.password = this.passwordEncoder.encode(account.password)
        accountRepo.save(account)
    }
}
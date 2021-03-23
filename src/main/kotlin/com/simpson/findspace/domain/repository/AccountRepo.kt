package com.simpson.findspace.domain.repository

import com.simpson.findspace.domain.model.h2.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepo: JpaRepository<Account, Long> {
    fun findByUserName(userName: String): Account?
}
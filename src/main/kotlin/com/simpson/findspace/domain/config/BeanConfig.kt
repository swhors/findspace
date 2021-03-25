package com.simpson.findspace.domain.config

import com.simpson.findspace.domain.model.h2.Account
import com.simpson.findspace.domain.model.h2.AccountRole
import com.simpson.findspace.domain.service.h2.AccountSvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.jvm.Throws

@Configuration
class BeanConfig {
    @Bean
    fun passwordEncoder() : PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun applicationRunner(): ApplicationRunner {
        return object : ApplicationRunner {

            @Autowired
            lateinit var accountSvc: AccountSvc

            @Throws(Exception::class)
            override fun run(args: ApplicationArguments) {
                accountSvc.saveAccount("admin@test.com", "password")
            }
        }
    }
}
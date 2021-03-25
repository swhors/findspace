package com.simpson.findspace.domain.controller

import com.simpson.findspace.domain.config.SearchProperty
import com.simpson.findspace.domain.config.security.JwtTokenProvider
import com.simpson.findspace.domain.model.h2.Account
import com.simpson.findspace.domain.model.h2.AccountRole
import com.simpson.findspace.domain.service.h2.AccountSvc
import org.junit.jupiter.api.BeforeEach
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.ArgumentMatchers.anyString
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
internal class UserCtlTest {
    @InjectMocks
    private lateinit var userCtl: UserCtl

    @Autowired
    @Mock
    private lateinit var passwordEncoder: PasswordEncoder
    
    @Autowired
    @Mock
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @Autowired
    @Mock
    private lateinit var accountSvc: AccountSvc

    @Autowired
    @Mock
    private lateinit var searchProperty: SearchProperty

    private val userName = "tester"
    private val password = "tester"
    private val wantedId = 1L
    private val wantedJWT = "abcdefghi"

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        Mockito.doAnswer{
            return@doAnswer password
        }.`when`(passwordEncoder).encode(anyString())

        Mockito.`when`(searchProperty.refreshIntervalMin()).thenReturn(10)
    }
    
    // 회원가입을 테스트 합니다.
    @Test
    fun testJoin() {
        Mockito.doAnswer { invocation ->
            val userName = invocation.arguments[0] as String
            val password = invocation.arguments[1] as String
            return@doAnswer Account.Builder()
                .userName(userName)
                .password(password)
                .roles(AccountRole.USER)
                .build()
        }.`when`(accountSvc).saveAccount(anyString(), anyString())

        val result = userCtl
            .joinInternal(userName,password)

        assertEquals((result !!.isNotEmpty()), true)
    }

    // 로그인을 테스트 합니다.
    @Test
    fun testLogin() {
        Mockito.doAnswer { invocation ->
            val userName1 = invocation.arguments[0]
            return@doAnswer Account.Builder()
                .userName(userName1 as String?)
                .password(password as String?)
                .roles(AccountRole.USER).build()
        }.`when`(accountSvc).findByUserName(anyString())

        Mockito.doAnswer{
            return@doAnswer (it.arguments[0] == it.arguments[1])
        }.`when`(passwordEncoder).matches(anyString(), anyString())

        Mockito.`when`(jwtTokenProvider.createToken(anyString(), anyString())).thenReturn(wantedJWT)

        val result = userCtl.login(hashMapOf("userName" to userName, "password" to password))
        print("testLogin jwt = $result\n")

        assertEquals((result?.length!! > 0), true)
    }
}
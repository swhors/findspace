package com.simpson.findspace.domain.controller

import com.simpson.findspace.domain.config.security.JwtTokenProvider
import com.simpson.findspace.domain.model.h2.Account
import com.simpson.findspace.domain.model.h2.AccountRole
import com.simpson.findspace.domain.repository.AccountRepo
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.junit.jupiter.api.BeforeEach
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.ArgumentMatchers.any
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
    @Spy
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @Autowired
    @Spy
    private lateinit var accountRepo: AccountRepo

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }
    
    private val userName = "tester"
    private val password = "tester"
    private val wantedId = 1L
	private val wantedJWT = "abcdefghi"
    
    // 회원가입
    @Test
    fun testJoin() {
        Mockito.`when`(passwordEncoder.encode(anyString())).thenReturn("ABDCDFEREWESDD")
        Mockito.doAnswer { invocation ->
            val inAccount = invocation.arguments[0] as Account
            print("testJoin inner = $inAccount")
            inAccount.id = wantedId
            return@doAnswer inAccount
        }.`when`(accountRepo.save(any()))

        val id = userCtl
            .join(hashMapOf("userName" to userName, "password" to password))

        assertEquals(id, wantedId)
    }

    // 로그인
    @Test
    fun testLogin() {
        Mockito.doAnswer { invocation ->
            val userName = invocation.arguments[0]
            val password = invocation.arguments[1]
            return@doAnswer Account.Builder()
				.userName(userName as String?)
				.password(password as String?)
				.roles(AccountRole.USER).build()
        }.`when`(accountRepo.findByUserName(anyString()))
		
		Mockito.`when`(passwordEncoder.encode(anyString())).thenReturn(password)
		
		Mockito.doAnswer { invocation ->
			return@doAnswer wantedJWT
		}.`when`(jwtTokenProvider.createToken(userName, "ROLE_USER"))
		
        val result = userCtl.login(hashMapOf("userName" to userName, "password" to password))
		print("testLogin jwt = $result")
		
		assertEquals((result?.length!! > 0), true)
    }
}
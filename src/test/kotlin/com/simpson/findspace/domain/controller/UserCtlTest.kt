package com.simpson.findspace.domain.controller

import com.simpson.findspace.domain.model.h2.SearchCache
import com.simpson.findspace.domain.model.h2.SearchHistory
import com.simpson.findspace.domain.service.SearchSvc
import com.simpson.findspace.domain.service.h2.SearchHistorySvc
import org.junit.jupiter.api.BeforeEach
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
internal class UserCtlTest {
	@InjectMocks
	private lateinit var userCtl: UserCtl

	@Autowired
	@Mock
	private val passwordEncoder: PasswordEncoder

	@Autowired
	@Mock
	private val jwtTokenProvider: JwtTokenProvider

	@Autowired
	@Mock
	private val accountRepo: AccountRepo

	@BeforeEach
	fun setUp() {
		MockitoAnnotations.openMocks(this)
	}

	// 회원가입
	@Test
	fun testJoin() {
	}
//	fun join(@RequestBody user: Map<String?, String?>): Long? {
//		return this.accountRepo.save(Account.Builder().
//		userName(user["userName"]).
//		password(passwordEncoder.encode(user["password"])).
//		roles(AccountRole.USER).build()).id
//	}

	// 로그인
	@Test
	fun testLogin() {
	}
//	fun login(@RequestBody user: Map<String?, String?>): String? {
//		val account: Account = user["userName"]?.let { accountRepo.findByUserName(it) }
//							   ?: throw java.lang.IllegalArgumentException("가입 되지 않은 사용자 입니다.")
//		val userName = user["userName"]
//		val password = user["password"]
//		print("login user=$userName, password=$password")
//		require(passwordEncoder.matches(user["password"], account.password)) {"잘못 된 비밀번호 입니다."}
//		return jwtTokenProvider.createToken(account.userName, account.roles!!.stream().toString())
//	}
}
package com.simpson.findspace.domain.service.api

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.simpson.findspace.domain.config.api.DaumConfig
import com.simpson.findspace.domain.repository.AccountRepo
import com.simpson.findspace.domain.repository.SearchCacheRepo
import com.simpson.findspace.domain.repository.SearchHistoryRepo
import org.junit.Before
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.MockitoAnnotations.initMocks
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.net.URL

@ExtendWith(SpringExtension::class)
@WebMvcTest(DaumSvc::class)
@AutoConfigureMockMvc(addFilters = false)
internal class DaumSvcTest {

	@Autowired
	private lateinit var daumSvc: DaumSvc

	@MockBean
	private lateinit var daumConfig: DaumConfig

	@MockBean
	private lateinit var accountRepo: AccountRepo

	@MockBean
	private lateinit var searchCacheRepo: SearchCacheRepo

	@MockBean
	private lateinit var searchHistoryRepo: SearchHistoryRepo

	@BeforeEach
	fun setUp() {
	}

	@Test
	fun searchPlace() {
		val keyword = "안산술집"
		val response =
			"{\"documents\":[{\"address_name\":\"경기 안산시 단원구 고잔동 539-3\","+
			"\"category_group_code\":\"FD6\",\"category_group_name\":\"음식점\","+
			"\"category_name\":\"음식점 \u003e 술집 \u003e 호프,요리주점\",\"distance\":\"\"," +
			"\"id\":\"846908683\",\"phone\":\"\",\"place_name\":\"평규술집 안산중앙점\"," +
			"\"place_url\":\"http://place.map.kakao.com/846908683\"," +
			"\"road_address_name\":\"경기 안산시 단원구 고잔로 112\"," +
			"\"x\":\"126.838553814052\",\"y\":\"37.3187008460709\"}]," +
			"\"meta\":{\"is_end\":false,\"pageable_count\":45," +
			"\"same_name\":{\"keyword\":\"$keyword\",\"region\":[],\"selected_region\":\"\"}," +
			"\"total_count\":1}}"
		whenever(daumSvc.getContent(any(), any(), any())).thenReturn(response)

		val result = daumSvc.searchPlace(keyword)
		print("$result")

	}
}
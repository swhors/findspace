package com.simpson.findspace.domain.service.api

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.simpson.findspace.domain.config.api.DaumConfig
import org.junit.Before
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.net.URL

@RunWith(MockitoJUnitRunner::class)
class DaumSvcTest {

	@InjectMocks
	private lateinit var daumSvc: DaumSvc

	@InjectMocks
	private lateinit var url: URL

	@InjectMocks
	private lateinit var daumConfig: DaumConfig

	@BeforeEach
	fun setUp() {
		MockitoAnnotations.initMocks(this)
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
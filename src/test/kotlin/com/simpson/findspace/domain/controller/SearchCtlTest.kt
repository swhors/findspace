package com.simpson.findspace.domain.controller

import com.simpson.findspace.domain.model.h2.SearchCache
import com.simpson.findspace.domain.model.h2.SearchHistory
import com.simpson.findspace.domain.service.SearchSvc
import com.simpson.findspace.domain.service.h2.SearchHistorySvc
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
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
internal class SearchCtlTest {
    @InjectMocks
    private lateinit var searchCtl: SearchCtl

    @Autowired
    @Mock
    private lateinit var historySvc: SearchHistorySvc

    @Autowired
    @Mock
    private lateinit var searchSvc: SearchSvc
    
    private val testUser = "a@test.com"
    private val testKeyWord = "a"
    
    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    val histories = arrayListOf(
            SearchHistory.Builder().keyword("a").userName("a@test.com").build(),
            SearchHistory.Builder().keyword("b").userName("a@test.com").build(),
            SearchHistory.Builder().keyword("c").userName("a@test.com").build(),
    )

    // 사용자의 히스토리를 가져오는 기능을 테스트 합니다.
    @Test
    fun getSearchHistory() {
        Mockito.`when`(historySvc.findTop10ByUserName(anyString())).thenReturn(histories)
        val result = searchCtl.getSearchHistoryInternal(testUser)
        print("getSearchHistory = $result\n")
        assertEquals((result.length > 0), true)
    }

    val favorities = arrayListOf(
            SearchCache.Build().hitcount(3).keyword("a").places("a").build(),
            SearchCache.Build().hitcount(2).keyword("b").places("b").build(),
            SearchCache.Build().hitcount(1).keyword("c").places("c").build()
    )

    // 자주찾는 키워드를 가져오는 것을 테스트 합니다.
    @Test
    fun getFavorite() {
        Mockito.`when`(searchSvc.getFavoriteKeyWord()).thenReturn(favorities)
        val result = searchCtl.getFavoriteHistory()
        print("getFavorite = $result\n")
        assertEquals((result.length > 0), true)
    }

    val places = arrayListOf(
            "COMMON_1", "COMMON_2", "DAUM_1", "DAUM_2", "NAVER_1", "NAVER_2"
    )

    // 키워드로 장소를 검색하는 것을 테스트 합니다.
    @Test
    fun searchPlace() {
        Mockito.`when`(searchSvc.searchPlace(anyString(), anyString())).thenReturn(places)
        val result = searchCtl.searchPlaceInternal(testUser, testKeyWord)
        print("searchPlace = $result\n")
        assertEquals((result.length > 0), true)
    }
}
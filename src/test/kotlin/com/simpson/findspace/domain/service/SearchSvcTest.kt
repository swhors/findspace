package com.simpson.findspace.domain.service

import com.simpson.findspace.domain.config.CacheProperty
import com.simpson.findspace.domain.model.h2.SearchCache
import com.simpson.findspace.domain.service.api.DaumSvc
import com.simpson.findspace.domain.service.api.NaverSvc
import com.simpson.findspace.domain.service.api.SearchApiSvc
import com.simpson.findspace.domain.service.h2.SearchCacheSvc
import org.junit.jupiter.api.BeforeEach
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.ArgumentMatchers.*
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
internal class SearchSvcTest {
    @InjectMocks
    private lateinit var searchSvc: SearchSvc

    @Autowired
    @Mock
    private lateinit var cacheSvc: SearchCacheSvc

    @Autowired
    @Mock
    private lateinit var daumSvc: DaumSvc

    @Autowired
    @Mock
    private lateinit var naverSvc: NaverSvc

    @Autowired
    @Spy
    private var searchApiSvcs: ArrayList<SearchApiSvc> = arrayListOf()

    @Autowired
    @Mock
    private lateinit var cacheProperty: CacheProperty

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    private val searchCaches = arrayListOf(
            SearchCache.Build().hitcount(3).keyword("A").places("A1").build(),
            SearchCache.Build().hitcount(2).keyword("B").places("B1").build(),
            SearchCache.Build().hitcount(1).keyword("C").places("C1").build()
    )

    private val searchCaches1 = SearchCache.Build()
            .hitcount(3)
            .keyword("A")
            .places("COMMON_2,COMMON_1,DAUM_1,DAUM_2,DAUM_3,NAVER_1,NAVER_2,NAVER_3")
            .build()

    private val daumResult = arrayListOf("DAUM_1", "DAUM_2", "COMMON_1", "DAUM_3", "COMMON_2")
    private val naverResult = arrayListOf("NAVER_1", "COMMON_2", "NAVER_2", "COMMON_1", "NAVER_3")

    private val wantedResult = arrayListOf("COMMON_2", "COMMON_1",
            "DAUM_1", "DAUM_2", "DAUM_3",
            "NAVER_1", "NAVER_2", "NAVER_3")

    @Test
    fun getFavoriteKeyWord() {
        Mockito.`when`(cacheSvc.getFavoriteKeyWord()).thenReturn(searchCaches)
        val result = searchSvc.getFavoriteKeyWord()
        print("getFavoriteKeyWord = $result\n")
        assertEquals(result!!.size, 3)
    }

    @Test
    fun searchRemote() {
        searchApiSvcs.clear()
        searchApiSvcs.add(daumSvc)
        searchApiSvcs.add(naverSvc)
        Mockito.`when`(daumSvc.searchPlace(anyString(), anyInt())).thenReturn(daumResult)
        Mockito.`when`(naverSvc.searchPlace(anyString(), anyInt())).thenReturn(naverResult)

        val result = searchSvc.searchRemote("A")
        print("searchRemote = $result\n")

        val wantedSize = 8

        assertEquals(result.size, wantedSize)
        assertEquals(result[0], "COMMON_2")
        assertEquals(result[1], "COMMON_1")
        assertEquals(result[2], "DAUM_1")
        assertEquals(result[5], "NAVER_1")
    }

    @Test
    fun searchLocal() {
        Mockito.`when`(cacheSvc.getCachedPlaces(anyString())).thenReturn(searchCaches1)
        val result = searchSvc.searchLocal("A")
        print("searchLocal = $result\n")
        assertEquals((result?.places?.length!! > 0), true)
    }

    @Test
    fun searchPlaceFromLocal() {
        Mockito.`when`(cacheSvc.getCachedPlaces(anyString())).thenReturn(searchCaches1)
        Mockito.`when`(cacheProperty.refreshIntervalMin()).thenReturn("10")
        val result = searchSvc.searchPlace("A")
        print("searchPlaceFromLocal = $result\n")
        assertEquals(result, wantedResult)
    }

    @Test
    fun searchPlaceFromRemote() {
        Mockito.`when`(cacheSvc.getCachedPlaces(anyString())).thenReturn(null)
        searchApiSvcs.clear()
        searchApiSvcs.add(daumSvc)
        searchApiSvcs.add(naverSvc)
        Mockito.`when`(daumSvc.searchPlace(anyString(), anyInt())).thenReturn(daumResult)
        Mockito.`when`(naverSvc.searchPlace(anyString(), anyInt())).thenReturn(naverResult)

        val result = searchSvc.searchPlace("A")
        print("searchPlaceFromLocal = $result\n")
        assertEquals(result, wantedResult)
    }
}
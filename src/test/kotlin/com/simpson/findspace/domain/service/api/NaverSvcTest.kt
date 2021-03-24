package com.simpson.findspace.domain.service.api

import com.simpson.findspace.domain.config.api.NaverConfig
import org.junit.jupiter.api.BeforeEach
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.ArgumentMatchers.anyMap
import org.mockito.ArgumentMatchers.anyString
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
internal class NaverSvcTest {

    @InjectMocks
    private lateinit var naverSvc: NaverSvc
    
    @Autowired
    @Mock
    private lateinit var naverConfig: NaverConfig
    
    @Autowired
    @Mock
    private lateinit var httpClient: HttpClient
    
    private val placeName = "역전할머니맥주 <b>안산</b>중앙역점"
    private val keyWord = "안산술집"
    
    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }
    
    private fun searchPlaceCore(rightCase : Boolean) : ArrayList<String> {
        val response = "{\"lastBuildDate\": \"Tue, 23 Mar 2021 23:22:14 +0900\"," +
                "\"total\": 5," +
                "\"start\": 1," +
                "\"display\": 5," +
                "\"items\": [{" +
                (if(rightCase) "\"title\": \"$placeName\"," else "")+
                "\"link\": \"\"," +
                "\"category\": \"술집>맥주,호프\"," +
                "\"description\": \"\"," +
                "\"telephone\": \"\"," +
                "\"address\": \"경기도 안산시 단원구 고잔동 539-13 1층\"," +
                "\"roadAddress\": \"경기도 안산시 단원구 고잔1길 67 1층\"," +
                "\"mapx\": \"297477\"," +
                "\"mapy\": \"524661\"}]}"
        Mockito.`when`(naverConfig.url()).thenReturn("https://api.daum.net")
        Mockito.`when`(naverConfig.clientId()).thenReturn("11111111")
        Mockito.`when`(naverConfig.clientSecret()).thenReturn("11111111")
        Mockito.`when`(httpClient.getContent(
                anyString(),
                anyString(),
                anyMap<String, String>() as HashMap<String,
                        String>)).thenReturn(response)
        return naverSvc.searchPlace(keyWord = keyWord, limit = 1) as ArrayList<String>
    }

    @Test
    fun searchPlaceRightCase() {
        val result = searchPlaceCore(true)
        print("naverSearchPlaceRightCase : size  = ${result.size}\n")
        print("naverSearchPlaceRightCase : item0 = ${result[0]}\n")
        assertTrue { result.size == 1 }
        assertTrue { result[0] == placeName }
    }
    
    @Test
    fun seachPlaceIllegalCase() {
        val result = searchPlaceCore(false)
        print("naverSeachPlaceIllegalCase : size  = ${result.size}")
        assertTrue { result.size == 0 }
    }
}
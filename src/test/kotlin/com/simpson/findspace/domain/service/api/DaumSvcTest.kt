package com.simpson.findspace.domain.service.api

import com.simpson.findspace.domain.config.api.DaumConfig
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyMap
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
internal class DaumSvcTest {

    @InjectMocks
    private lateinit var daumSvc: DaumSvc
    
    @Autowired
    @Mock
    private lateinit var daumConfig: DaumConfig
    
    @Autowired
    @Mock
    private lateinit var httpClient: HttpClient
    
    private val keyword = "안산술집"
    private val placeName = "평규술집 안산중앙점"
    
    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }
    
    private fun searchPlaceCore(rightCase: Boolean) : ArrayList<String> {
        val response = "{\"documents\":[{" +
                "\"address_name\":\"경기 안산시 단원구\","+
                "\"category_group_code\":\"FD\"," +
                "\"category_group_name\":\"음식점\","+
                "\"category_name\":\"음식점 술집 호프,요리주점\"," +
                "\"distance\":\"\"," +
                "\"id\":\"846908683\"," +
                "\"phone\":\"\"," +
                (if (rightCase) "\"place_name\":\"$placeName\"," else "") +
                "\"place_url\":\"http://place.map.kakao.com/908683\"," +
                "\"road_address_name\":\"경기 안산시 단원구\"," +
                "\"x\":\"12.83814052\"," +
                "\"y\":\"3.318460709\"}]," +
              "\"meta\":{" +
                "\"is_end\":false," +
                "\"pageable_count\":45," +
                "\"same_name\":{" +
                  "\"keyword\":\"$keyword\"," +
                  "\"region\":[]," +
                  "\"selected_region\":\"\"}," +
                "\"total_count\":1" +
              "}}"
        Mockito.`when`(daumConfig.url()).thenReturn("https://api.daum.net")
        Mockito.`when`(daumConfig.restApiKey()).thenReturn("11111111")
        Mockito.`when`(httpClient.getContent(anyString(),
                anyString(),
                anyMap<String, String>() as HashMap<String,String>)).thenReturn(response)
        return daumSvc.searchPlace(keyWord = keyword, limit = 1) as ArrayList<String>
    }
    
    // 장소 검색이 정상적으로 되는 것을 테스트 합니다.
    @Test
    fun searchPlaceRightCase() {
        val result = searchPlaceCore(true)
        print("daumSearchPlaceRightCase : size  = ${result.size}\n")
        print("daumSearchPlaceRightCase : item0 = ${result[0]}\n")
        assertTrue(result.size == 1)
        assertEquals(result[0], placeName)
    }
    
    // 장소 검색이 정상적으로 되지 않은 경우의 출력을 테스트 합니다.
    @Test
    fun searchPlaceIllegalCase() {
        val result = searchPlaceCore(false)
        print("daumSearchPlaceRightCase : size  = ${result.size}\n")
        assertTrue(result.size == 0)
    }
}
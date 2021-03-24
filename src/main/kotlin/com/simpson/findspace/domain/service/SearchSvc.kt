package com.simpson.findspace.domain.service

import com.simpson.findspace.domain.model.h2.SearchCache
import com.simpson.findspace.domain.service.api.SearchApiSvc
import com.simpson.findspace.domain.service.h2.SearchCacheSvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

@Service
class SearchSvc(@Autowired val searchApiSvcs: List<SearchApiSvc>,
                @Autowired val cacheSvc: SearchCacheSvc) {

    @Value("#{apps.cache.refresh.interval.min}")
    private var refreshIntervalMin : Int = 10

    fun getFavoriteKeyWord() : ArrayList<SearchCache> ? {
        return cacheSvc.getFavoriteKeyWord()
    }

    fun searchLocal(keyword: String) : SearchCache?{
        return cacheSvc.getCachedPlaces(keyword)
    }

    /***
     * 설명 :
     *   DaymApi와 NaverApi를 이용하여 상호명을 반환 한다.
     *   반환 하는 값의 조건은 다음과 같다.
     *   - 다음의 검색 값을 앞에 위치시키며,
     *   - 네이버의 검색 값을 뒤에 놓는다.
     *   - 단, 두 개의 검색에서 공통으로 나오는 경우는 그 값을 먼저 놓는다.
     *
     * Params
     *  - keyword : String, 검색할 키워드
     *
     * Return
     *  - ArrayList<String>, 검색 결과
     */

    fun searchRemote(keyword: String) : ArrayList<String> {
        val secondResultCols = ArrayList<String>()
        val firstResultCols = ArrayList<String>()
        searchApiSvcs.forEach { search ->
            val results = search.searchPlace(keyWord = keyword)
            print("results = $results\n")
            for (result in results) {
                if (secondResultCols.contains(result)) {
                    secondResultCols.remove(result)
                    firstResultCols.add(result)
                } else {
                    secondResultCols.add(result)
                }
            }
        }

        print("first  results = $firstResultCols\n")
        print("second results = $secondResultCols\n")
        return (firstResultCols + secondResultCols) as ArrayList<String>
    }

    /***
     * 설명:
     *   이 함수는 사용자의 키워드를 입력 받아서 위치 리스트를 반환합니다.
     *   위치 리스트는 먼저 자신의 H2에서 검색을 하고, 없을 경우에 Daum-Api와
     *   Naver-Api를 이용하여 검색을 한다.
     *   검색 된 값은 자신의 H2에 저장한다.
     *
     *   H2에 저장된 값을 가져오는 경우에도, 저장된 값이 10전의 데이터인 경우에는
     *   다시 Daum과 Naver를 이용하여 가져 온다. 그리고, 이 값을 다시 H2에 업데이트
     *   한다.
     *
     * Params:
     *  - keyword : String, 검색하고자 하는 키워드
     * 
     * Return :
     *  - Place List : 조회한 위치 리스트
     */
    @Transactional
    fun searchPlace(keyword: String) : ArrayList<String> {
        val cache : SearchCache? = searchLocal(keyword)
        var createCache = true
        var result = cache?.let{
            val dtNow = LocalTime.now().minute
            val dtUp = it.updated.toLocalTime().minute + refreshIntervalMin
            createCache = false
            if (dtNow < dtUp) {
                cacheSvc.updateHitCount(keyword)
                return@let it.places.split(",") as ArrayList<String>
            }
            return@let arrayListOf<String>()
        }

        if (result == null || result.size == 0) {
            result = searchRemote(keyword)
            val resultStr = result.joinToString(",")
            if (createCache) {
                cacheSvc.saveCache(keyword, resultStr)
            } else {
                cacheSvc.updateCachedPlaces(keyword, resultStr)
            }
        }

        print("searchPlace == $result\n")

        return result
    }
}
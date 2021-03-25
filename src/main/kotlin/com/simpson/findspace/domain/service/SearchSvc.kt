package com.simpson.findspace.domain.service

import com.simpson.findspace.domain.config.SearchProperty
import com.simpson.findspace.domain.model.h2.SearchCache
import com.simpson.findspace.domain.service.api.SearchApiSvc
import com.simpson.findspace.domain.service.h2.SearchCacheSvc
import com.simpson.findspace.domain.service.h2.SearchHistorySvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

@Service
class SearchSvc(@Autowired val searchApiSvcs: List<SearchApiSvc>,
                @Autowired val cacheSvc: SearchCacheSvc,
                @Autowired val searchHistorySvc: SearchHistorySvc,
                @Autowired val searchProperty: SearchProperty) {

    fun getFavoriteKeyWord() : ArrayList<SearchCache> ? {
        return cacheSvc.getFavoriteKeyWord()
    }

    fun searchLocal(keyword: String) : SearchCache?{
        return cacheSvc.getCachedPlaces(keyword)
    }
    
    /***
     * 설명 :
     *   두개의 리스트의 교집합을 반환 합니다.
     */
    private fun intersection(data1: List<String>, data2: List<String>) : ArrayList<String> {
        val commons = ArrayList<String>()
        
        data1.forEach { it ->
            if (data2.contains(it)) commons.add(it)
        }
        return commons
    }

    /***
     * 설명 :
     *   DaymApi와 NaverApi를 이용하여 상호명을 반환 한다.
     *   반환 하는 값의 조건은 다음과 같다.
     *   - 다음의 검색 값을 앞에 위치시키며,
     *   - 네이버의 검색 값을 뒤에 놓는다.
     *   - 단, 두 개의 검색에서 공통으로 나오는 경우는 그 값을 먼저 놓는다.
     *   - 공통 데이터의 순서는 다음검색 결과와 동일해야 한다.
     *
     * Params
     *  - keyword : String, 검색할 키워드
     *
     * Return
     *  - ArrayList<String>, 검색 결과
     */
    fun searchRemote(keyword: String) : ArrayList<String> {
        val placesCols = ArrayList<List<String>>()
        
        /* 데이터를 수집한다. */
        searchApiSvcs.forEach { search ->
            placesCols.add(search.searchPlace(keyWord = keyword))
        }
        
        /* 교집합 데이터를 찾는다. */
        val common =
            if (placesCols.size == 2)
                intersection(placesCols[0], placesCols[1])
            else if (placesCols.size > 0)
                placesCols[0]
            else
                arrayListOf()
        
        /* 모든 데이터에서 교집합 데이터를 제거 한다. */
        common.forEach { it ->
            with(it) {
                (placesCols[0] as ArrayList<String>).remove(it)
                (placesCols[1] as ArrayList<String>).remove(it)
            }
        }
        
        /* 데이터를 반환한다. */
        return (common + placesCols[0] + placesCols[1]) as ArrayList<String>
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
    fun searchPlace(keyword: String, userName: String) : ArrayList<String> {
        val cache : SearchCache? = searchLocal(keyword)
        var createCache = true
        // 로컬 DB 검색
        var result = cache?.let{
            val dtNow = LocalTime.now().minute
            val dtUp = it.updated.toLocalTime().minute + searchProperty.refreshIntervalMin()
            createCache = false
            if (dtNow < dtUp) {
                //데이터가 유효 한 경우에, hitcount를 증가 시키고 이 데이터를 반환 합니다.
                cacheSvc.updateHitCount(keyword)
                return@let it.places.split(",") as ArrayList<String>
            }
            return@let arrayListOf<String>()
        }
        //데이터가 유효 기간을 넘겼거나 없을 경우에 서버에서 검색
        if (result == null || result.size == 0) {
            result = searchRemote(keyword)
            val resultStr = result.joinToString(",")
            if (createCache) {
                // 없을 경우에 캐쉬 생성
                cacheSvc.saveCache(keyword, resultStr)
            } else {
                // 유효 기간을 넘긴 경우는 캐쉬 업데이트
                cacheSvc.updateCachedPlaces(keyword, resultStr)
            }
        }
        
        // 사용자 ID와 KeyWord로 SearchHistory 테이블에 저장
        if (searchProperty.historyAll) {
            // 검색 실패와 성공의 모든 키워드를 남깁니다.
            searchHistorySvc.saveUserHistory(userName, keyword)
        } else if (result.size > 0) {
            // 검색 성공인 경우에만 키워드를 남깁니다.
            searchHistorySvc.saveUserHistory(userName, keyword)
        }

        return result
    }
}
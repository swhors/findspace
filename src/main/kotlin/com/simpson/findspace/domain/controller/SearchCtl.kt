package com.simpson.findspace.domain.controller

import com.google.gson.Gson
import com.simpson.findspace.domain.model.local.*
import com.simpson.findspace.domain.service.h2.SearchHistorySvc
import com.simpson.findspace.domain.service.SearchSvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.lang.Exception
import java.net.URLDecoder

@RestController
@RequestMapping("/api")
class SearchCtl(@Autowired val historySvc: SearchHistorySvc,
                @Autowired val searchSvc: SearchSvc) {
    
    /***
     * 설명 :
     *   DB에서 history를 읽어 와서 사용자에게 전달
     * Params:
     *   userName - String, 사용자 id
     * Return:
     *   String 타입의 JSON 데이터
     */
    fun getSearchHistoryInternal(userName: String) : String {
        try {
            val historyList = ArrayList<HistoryResult>()

            //DB에서 히스토리를 읽어 옴
            val histories = historySvc.findTop10ByUserName(userName)

            //DB에서 읽은 데이터를 Result 형태로 변환
            histories?.forEach {
                historyList.add(HistoryResult
                    .Builder()
                    .keyword(URLDecoder.decode(it.keyword, "UTF-8"))
                    .created(it.createDt.toString())
                    .build())
            }

            //Results로 변환 후, JSON으로 변환하여 전달
            return Gson().toJson(HistoryResults
                .Builder()
                .length(historyList.size)
                .histories(historyList)
                .build())
        } catch (e: Exception) {
            return Gson().toJson(Error(500, e.message.toString()))
        }
    }
    
    @GetMapping("/search/history")
    fun getSearchHistory() : String {
        return getSearchHistoryInternal(
                SecurityContextHolder.getContext()!!.authentication!!.name)
    }
    
    /***
     * 설명 :
     *   DB에서 FavoriteList를 읽어 와서 사용자에게 전달
     * Params:
     *   - none
     * Return:
     *   String 타입의 JSON 데이터
     */
    @GetMapping("/search/favorite")
    fun getFavoriteHistory() : String {
        try {
            val favoriteList = ArrayList<FavoriteResult>()

            // 인기 키워드를 검색하여 리턴의 items 형태로 변환
            val favorities = searchSvc.getFavoriteKeyWord()
            favorities?.forEach {
                favoriteList.add(FavoriteResult
                    .Builder()
                    .hitCount(it.hitcount)
                    .keyword(it.keyword).build())
            }

            return Gson()
                .toJson(FavoriteResults
                    .Builder()
                    .length(favoriteList.size)
                    .favorities(favoriteList).build())
        } catch (e: Exception) {
            return Gson().toJson(Error(500, e.message.toString()))
        }
    }
    
    /***
     * 설명 :
     *   사용자 키워드로 Place를 검색하여 사용자에게 전달
     * Params:
     *   - userName - String, 사용자 id
     *   - keyword  - String, 검색 키워드
     * Return:
     *   String 타입의 JSON 데이터
     */
    fun searchPlaceInternal(userName: String, keyword: String) : String {
        try {
            val placeList = ArrayList<PlaceResult>()

            // 사용자 키워드 검색
            val places = searchSvc.searchPlace(keyword, userName)
            // ArrayList<PlaceResult>의 데이터 변환
            places.forEach {
                placeList.add(PlaceResult.Builder().place(it).build())
            }

            // PlaceResuls를 생성 후에 JSON으로 변환하여 전달
            return Gson()
                .toJson(PlaceResults
                    .Builder()
                    .keyword(keyword)
                    .length(placeList.size)
                    .places(placeList)
                    .build())
        } catch (e: Exception) {
            return Gson().toJson(Error(500, e.message.toString()))
        }
    }

    @GetMapping("/search/place")
    fun searchPlace(@RequestParam("keyword") keyword: String) : String{
        return searchPlaceInternal(
                SecurityContextHolder.getContext().authentication.name,
                keyword)
    }
}
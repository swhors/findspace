package com.simpson.findspace.domain.controller

import com.google.gson.Gson
import com.simpson.findspace.domain.model.local.*
import com.simpson.findspace.domain.service.h2.SearchHistorySvc
import com.simpson.findspace.domain.service.SearchSvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.net.URLDecoder

@RestController
@RequestMapping("/api")
class SearchCtl(@Autowired val historySvc: SearchHistorySvc,
                @Autowired val searchSvc: SearchSvc) {
    @GetMapping("/search/history/{username}")
    fun getSearchHistory(@PathVariable("username") userName : String) : String {
        val histories = historySvc.findTop10ByUserName(userName)
        val historyList = ArrayList<HistoryResult>()
        histories!!.forEach {
            historyList.add(HistoryResult.
                                Builder().
                                keyword(URLDecoder.decode(it.keyword, "UTF-8")).
                                created(it.createDt.toString()).
                                build())
        }
        val results = HistoryResults.Builder().length(historyList.size).
                                    histories(historyList).build()
        val gson = Gson()
        return gson.toJson(results)
    }

    @GetMapping("/search/favorite")
    fun getFavoriteHistory() : String {
        val favorities = searchSvc.getFavoriteKeyWord()
        val favoriteList = ArrayList<FavoriteResult>()
        favorities!!.forEach {
            favoriteList.add(FavoriteResult.Builder().hitCount(it.hitcount).keyword(it.keyword).build())
        }
        return Gson().toJson(FavoriteResults.Builder().length(favoriteList.size).favorities(favoriteList).build())
    }

    @GetMapping("/search/place/{keyword}")
    fun searchPlace(@PathVariable("keyword") keyword: String) : String{
        val places = searchSvc.searchPlace(keyword)
        val placeList = ArrayList<PlaceResult>()
        places.forEach {
            placeList.add(PlaceResult.Builder().place(it).build())
        }

        return Gson().toJson(PlaceResults.Builder().keyword(keyword).length(placeList.size).places(placeList).build())
    }
}
package com.simpson.findspace.domain.service

import com.simpson.findspace.domain.model.h2.SearchCache
import com.simpson.findspace.domain.service.api.SearchApiSvc
import com.simpson.findspace.domain.service.h2.SearchCacheSvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

@Service
class SearchSvc(@Autowired val searchApiSvcs: List<SearchApiSvc>,
                @Autowired val cacheSvc: SearchCacheSvc) {

    fun getFavoriteKeyWord() : ArrayList<SearchCache> ? {
        return cacheSvc.getFavoriteKeyWord()
    }

    fun searchLocal(keyword: String) : SearchCache?{
        return cacheSvc.getCachedPlaces(keyword)
    }

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

    fun searchPlace(keyword: String) : ArrayList<String> {
        val cache : SearchCache? = searchLocal(keyword)
        var createCache = true
        var result = cache?.let{
            val dtNow = LocalTime.now().minute
            val dtUp = it.updated.toLocalTime().minute + 10
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
package com.simpson.findspace.domain.service.h2

import com.simpson.findspace.domain.model.h2.SearchCache
import com.simpson.findspace.domain.repository.SearchCacheRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SearchCacheSvc(@Autowired val seachCacheRepo: SearchCacheRepo) {
    fun updateHitCount(keyWord: String) {
        try {
            seachCacheRepo.getSearchCacheByKeyword(keyWord)?.let {
                it.hitcount ++
                seachCacheRepo.save(it)
            }
        } catch (e: Exception) {
            print("$e\n")
        }
    }

    fun getCachedPlaces(keyWord: String) : SearchCache? {
        try {
            return seachCacheRepo.getSearchCacheByKeyword(keyWord)
        } catch (e: Exception) {
            print("$e\n")
        }
        return null
    }

    fun updateCachedPlaces(keyWord: String, places: String) {
        try {
            seachCacheRepo.getSearchCacheByKeyword(keyWord)?.let {
                it.places = places
                it.hitcount ++
                seachCacheRepo.save(it)
            }
        } catch (e : Exception) {
            print("$e")
        }
    }

    fun saveCache(keyWord: String, places: String) {
        seachCacheRepo.save(SearchCache(keyword = keyWord, places = places, hitcount = 1))
    }

    fun getFavoriteKeyWord() : ArrayList<SearchCache> ?{
        try {
            return seachCacheRepo.getFavoriteKeyWord()
        } catch (e: Exception) {
            print("$e\n")
        }
        return null
    }
}
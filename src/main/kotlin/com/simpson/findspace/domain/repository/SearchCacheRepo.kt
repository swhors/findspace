package com.simpson.findspace.domain.repository

import com.simpson.findspace.domain.model.h2.SearchCache
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface SearchCacheRepo : JpaRepository<SearchCache, Long>{
    fun getSearchCacheByKeyword(keyword: String) : SearchCache ?

    @Query("select * from search_cache order by hitcount desc limit 10", nativeQuery = true)
    fun getFavoriteKeyWord() : ArrayList<SearchCache> ?
}
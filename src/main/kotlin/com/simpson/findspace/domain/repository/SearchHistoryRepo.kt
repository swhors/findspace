package com.simpson.findspace.domain.repository

import com.simpson.findspace.domain.model.h2.SearchHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.lang.Nullable

interface SearchHistoryRepo : JpaRepository<SearchHistory, Long> {

    @Nullable
    @Query("select * from search_history where user_name=:user_name order by create_dt desc limit 10",
           nativeQuery = true)
    fun getSearchHistoriesByUserNameLimit10(@Param("user_name") user_name: String) : ArrayList<SearchHistory> ?

    @Query("select * from search_history", nativeQuery = true)
    fun getAll() : ArrayList<SearchHistory>
}
package com.simpson.findspace.domain.service.h2

import com.simpson.findspace.domain.model.h2.SearchHistory
import com.simpson.findspace.domain.repository.SearchHistoryRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SearchHistorySvc(@Autowired val searchHistoryRepo: SearchHistoryRepo) {
    fun findTop10ByUserName(userName: String) : ArrayList<SearchHistory> ? {
        return searchHistoryRepo.getSearchHistoriesByUserNameLimit10(userName)
    }

    fun saveUserHistory(userName: String, keyword: String) {
        searchHistoryRepo.save(SearchHistory(userName, keyword))
    }
}
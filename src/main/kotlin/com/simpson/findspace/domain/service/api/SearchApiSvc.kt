package com.simpson.findspace.domain.service.api

interface SearchApiSvc {
    fun searchPlace(keyWord: String, limit: Int = 10) : List<String>
}
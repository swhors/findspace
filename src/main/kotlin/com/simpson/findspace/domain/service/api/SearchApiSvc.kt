package com.simpson.findspace.domain.service.api

interface SearchApiSvc {
    fun searchPlace(keyWord: String) : List<String>
}
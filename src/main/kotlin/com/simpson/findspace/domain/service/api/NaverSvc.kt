package com.simpson.findspace.domain.service.api

import com.google.gson.Gson
import com.simpson.findspace.domain.config.api.NaverConfig
import com.simpson.findspace.domain.model.api.NaverPlaceResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.net.URLEncoder


@Component
@Order(2)
class NaverSvc(@Autowired val naverConfig: NaverConfig): SearchApiSvc, HttpClient() {
    override fun searchPlace(keyWord: String) : List<String>{
        val results =  ArrayList<String>()
        val newKeyWord = URLEncoder.encode(keyWord, "UTF-8")
        val uri = "?query=\"$newKeyWord\"&display=10&start=1"

        val response = getContent(naverConfig.url,
                                  uri,
                                  hashMapOf(
                                    "X-Naver-Client-Id" to naverConfig.clientId,
                                    "X-Naver-Client-Secret" to naverConfig.clientSecret,
                                    "Content-Type" to "application/json",
                                    "User-Agent" to "findspace/1.0",
                                    "Accept" to "*/*",
                                    "Connection" to "keep-alive"))

        print("naver readed = ${response}\n")

        val placeResult = Gson().fromJson(response, NaverPlaceResult::class.java)

        for( item in placeResult.items) {
            results.add(item.title.toString())
        }
        return results
    }
}
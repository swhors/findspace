package com.simpson.findspace.domain.service.api

import com.google.gson.Gson
import com.simpson.findspace.domain.config.api.DaumConfig
import com.simpson.findspace.domain.model.api.DaumPlaceResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.net.URLEncoder

@Component
@Order(1)
class DaumSvc(@Autowired val daumConfig: DaumConfig) : SearchApiSvc, HttpClient() {
    override fun searchPlace(keyWord: String): List<String> {
        val results =  ArrayList<String>()
        val newKeyWord = URLEncoder.encode(keyWord, "UTF-8")
        val uri = "?query=\"$newKeyWord\"&size=10&page=1"
        val response = super.getContent(daumConfig.url,
                                        uri,
                                        hashMapOf("Authorization" to "KakaoAK " + daumConfig.restApiKey,
                                                  "Cokkie" to "kd_lang=ko",
                                                  "Content-Type" to "application/json",
                                                  "User-Agent" to "findspace/1.0",
                                                  "Accept" to "*/*",
                                                  "Connection" to "keep-alive"))

        print("daum readed = ${response}\n")

        val placeResult = Gson().fromJson(response, DaumPlaceResult::class.java)

        for( document in placeResult.documents) {
            results.add(document.place_name)
        }

        return results
    }
}
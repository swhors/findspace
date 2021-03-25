package com.simpson.findspace.domain.service.api

import com.google.gson.Gson
import com.simpson.findspace.domain.config.api.DaumConfig
import com.simpson.findspace.domain.model.api.DaumPlaceResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.util.ObjectUtils
import java.net.URLEncoder

@Component
@Order(1)
class DaumSvc(@Autowired val daumConfig: DaumConfig,
              @Autowired val httpClient: HttpClient) : SearchApiSvc {
    override fun searchPlace(keyWord: String, limit: Int): List<String> {
        val results =  ArrayList<String>()
        val newKeyWord = URLEncoder.encode(keyWord, "UTF-8")
        val uri = "?query=\"$newKeyWord\"&size=10&page=1"
        val url = daumConfig.url()
        val key = "KakaoAK " + daumConfig.restApiKey()
        val properties = hashMapOf("Authorization" to key,
                "Cokkie" to "kd_lang=ko",
                "Content-Type" to "application/json",
                "User-Agent" to "findspace/1.0",
                "Accept" to "*/*",
                "Connection" to "keep-alive")
        
        val response = httpClient.getContent(url, uri, properties)

        print("daum readed = ${response}\n")

        val placeResult = Gson().fromJson(response, DaumPlaceResult::class.java)
    
        placeResult.documents.forEach {
            if (!ObjectUtils.isEmpty(it.place_name)) {
                results.add(it.place_name)
            }
        }

        return results
    }
}
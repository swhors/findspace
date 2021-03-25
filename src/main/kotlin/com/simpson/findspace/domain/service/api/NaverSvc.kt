package com.simpson.findspace.domain.service.api

import com.google.gson.Gson
import com.simpson.findspace.domain.config.api.NaverConfig
import com.simpson.findspace.domain.model.api.NaverPlaceResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.util.ObjectUtils
import java.net.URLEncoder


@Component
@Order(1)
class NaverSvc(@Autowired val naverConfig: NaverConfig,
               @Autowired val httpClient: HttpClient): SearchApiSvc {
    override fun searchPlace(keyWord: String, limit: Int) : List<String>{
        val results =  ArrayList<String>()
        val newKeyWord = URLEncoder.encode(keyWord, "UTF-8")
        val uri = "?query=\"$newKeyWord\"&display=10&start=1"
        val url = naverConfig.url()
        val clientId = naverConfig.clientId()
        val clientSecret = naverConfig.clientSecret()
        val properties = hashMapOf(
                "X-Naver-Client-Id" to clientId,
                "X-Naver-Client-Secret" to clientSecret,
                "Content-Type" to "application/json",
                "User-Agent" to "findspace/1.0",
                "Accept" to "*/*",
                "Connection" to "keep-alive")

        val response = httpClient.getContent(url, uri, properties)

        print("naver readed = ${response}\n")

        val placeResult = Gson().fromJson(response, NaverPlaceResult::class.java)
    
        placeResult.items.forEach{
            if (!ObjectUtils.isEmpty(it.title))
                it.title?.let { it1 -> results.add(it1) }
        }
        return results
    }
}
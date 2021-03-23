package com.simpson.findspace.domain.service.api

import com.google.gson.Gson
import com.simpson.findspace.domain.config.api.NaverConfig
import com.simpson.findspace.domain.model.api.NaverPlaceResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


@Component
@Order(2)
class NaverSvc(@Autowired val naverConfig: NaverConfig): SearchApiSvc {
    override fun searchPlace(keyWord: String) : List<String>{
        val results =  ArrayList<String>()
        val newKeyWord = URLEncoder.encode(keyWord, "UTF-8")
        val urls = naverConfig.url + "?query=\"$newKeyWord\"&display=10&start=1&sort=random"
        val url = URL(urls)
        val con = url.openConnection() as HttpURLConnection
        con.setRequestMethod("GET")

        con.setRequestProperty("X-Naver-Client-Id", naverConfig.clientId)
        con.setRequestProperty("X-Naver-Client-Secret", naverConfig.clientSecret)
        con.setRequestProperty("Content-Type", "application/json")
        con.setRequestProperty("User-Agent", "PostmanRuntime/7.26.10")
        con.setRequestProperty("Accept", "*/*")
        con.setRequestProperty("Connection", "keep-alive")
        var br : BufferedReader
        if (con.responseCode == 200) {
            br = BufferedReader(InputStreamReader(con.inputStream))
        } else {
            br = BufferedReader(InputStreamReader(con.errorStream))
        }

        val response = StringBuffer()
        var inputLine = br.readLine()
        while(inputLine != null) {
            response.append(inputLine)
            inputLine = br.readLine()
        }

        print("naver readed = ${response.toString()}\n")

        val placeResult = Gson().fromJson(response.toString(), NaverPlaceResult::class.java)

        br.close()
        for( item in placeResult.items) {
            results.add(item.title.toString())
        }
        return results
    }
}
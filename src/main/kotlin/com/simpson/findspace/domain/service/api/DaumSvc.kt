package com.simpson.findspace.domain.service.api

import com.google.gson.Gson
import com.simpson.findspace.domain.config.api.DaumConfig
import com.simpson.findspace.domain.model.api.DaumPlaceResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

@Component
@Order(1)
class DaumSvc(@Autowired val daumConfig: DaumConfig) : SearchApiSvc {
    override fun searchPlace(keyWord: String): List<String> {
        val results =  ArrayList<String>()
        val newKeyWord = URLEncoder.encode(keyWord, "UTF-8")
        val urls = daumConfig.url + "?query=\"$newKeyWord\"&size=10&page=1"
        val url = URL(urls)
        val con = url.openConnection() as HttpURLConnection
        con.setRequestMethod("GET")

        con.setRequestProperty("Authorization", "KakaoAK " + daumConfig.restApiKey)
        con.setRequestProperty("Cokkie", "kd_lang=ko")
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

        print("daum readed = ${response.toString()}\n")

        val placeResult = Gson().fromJson(response.toString(), DaumPlaceResult::class.java)

        br.close()
        for( document in placeResult.documents) {
            results.add(document.place_name)
        }
        return results
    }
}
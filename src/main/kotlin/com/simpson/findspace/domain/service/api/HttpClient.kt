package com.simpson.findspace.domain.service.api

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

open class HttpClient {
    fun getContent(address: String, uri: String, properties: HashMap<String, String>) : String {
        val url = URL(address + uri)
        val con = url.openConnection() as HttpURLConnection

        with(con) {
            requestMethod = "GET"
            for (key in properties.keys) {
                setRequestProperty(key, properties[key])
            }
        }

        val br = if (con.responseCode == 200) {
                   BufferedReader(InputStreamReader(con.inputStream))
               } else {
            BufferedReader(InputStreamReader(con.errorStream))
        }

        val response = StringBuffer()
        var inputLine = br.readLine()

        while(inputLine != null) {
            response.append(inputLine)
            inputLine = br.readLine()
        }

        br.close()

        return response.toString()
    }
}
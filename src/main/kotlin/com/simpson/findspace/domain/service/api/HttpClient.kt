package com.simpson.findspace.domain.service.api

import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

@Component
class HttpClient {
    fun getContent(address: String, uri: String, properties: HashMap<String, String>) : String {
        val url = URL(address + uri)
        val con = url.openConnection() as HttpURLConnection
        
        with(con) {
            requestMethod = "GET"
            for (key in properties.keys) {
                setRequestProperty(key, properties[key])
            }
        }
        
        val response = StringBuffer()
    
        val br = (if (con.responseCode == 200) {
            BufferedReader(InputStreamReader(con.inputStream))
        } else {
            BufferedReader(InputStreamReader(con.errorStream))
        })
        
        br.let {
            var inputLine = it.readLine()
            while(inputLine != null) {
                response.append(inputLine)
                inputLine = it.readLine()
            }
            it.close()
        }
        
        return response.toString()
    }
}
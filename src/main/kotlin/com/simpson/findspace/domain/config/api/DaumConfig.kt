package com.simpson.findspace.domain.config.api

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "apps.daum")
class DaumConfig {
    lateinit var restApiKey: String
    lateinit var url: String
    
    fun restApiKey() = restApiKey
    fun url() = url
}
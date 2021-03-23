package com.simpson.findspace.domain.config.api

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "apps.naver")
class NaverConfig {
    lateinit var clientId: String
    lateinit var clientSecret: String
    lateinit var url: String

}
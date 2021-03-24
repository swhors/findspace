package com.simpson.findspace.domain.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "apps.cache")
class CacheProperty {
    lateinit var refreshIntervalMin: String

    fun refreshIntervalMin() = refreshIntervalMin
}
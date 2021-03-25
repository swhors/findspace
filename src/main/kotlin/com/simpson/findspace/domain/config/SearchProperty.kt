package com.simpson.findspace.domain.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "apps.search")
class SearchProperty {
    var refreshIntervalMin: Int = 60
    var historyAll: Boolean = false
    
    fun refreshIntervalMin() = refreshIntervalMin
    fun historyAll() = historyAll
    fun historyAll(historyAll: Boolean) = apply { this.historyAll = historyAll }
}
package com.simpson.findspace.domain.config.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "apps.jwt")
class JwtProperty {
    lateinit var secretkey: String
    lateinit var duration: String

    fun duration() = duration
    fun secretkey() = secretkey
}
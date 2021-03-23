package com.simpson.findspace

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@ComponentScan("com.simpson.findspace.domain")
@SpringBootApplication
@ConfigurationPropertiesScan
class FindspaceApplication

fun main(args: Array<String>) {
    runApplication<FindspaceApplication>(*args)
}

package com.dashboard.demo.client.common

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class RestClientResolver(
    private val restTemplateBuilder: RestTemplateBuilder
) {
    fun get(url: String): String? {
        return restTemplateBuilder.build().getForObject(url, String::class.java)
    }
}
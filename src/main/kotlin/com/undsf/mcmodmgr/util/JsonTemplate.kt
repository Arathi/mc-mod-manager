package com.undsf.mcmodmgr.util

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired

private val log = KotlinLogging.logger {}

class JsonTemplate {
    @Autowired
    lateinit var mapper: ObjectMapper

    fun stringify(obj: Any) : String? {
        try {
            return mapper.writeValueAsString(obj)
        }
        catch (ex: JsonProcessingException) {
            log.warn { "JSON序列化发生异常" }
        }
        return null
    }
}

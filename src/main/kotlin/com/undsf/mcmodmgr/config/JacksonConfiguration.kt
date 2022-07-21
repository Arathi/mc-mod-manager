package com.undsf.mcmodmgr.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Configuration
class JacksonConfiguration {
    @Bean
    fun getObjectMapper(): ObjectMapper {
        var objectMapper = jacksonObjectMapper()
        val jsr310 = JavaTimeModule()
        val format = DateTimeFormatter.ISO_DATE_TIME
        jsr310.addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(format))
        jsr310.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(format))

        objectMapper.registerModule(jsr310)
        return objectMapper
    }
}

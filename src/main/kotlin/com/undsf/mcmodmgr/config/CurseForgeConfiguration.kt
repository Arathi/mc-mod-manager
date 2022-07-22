package com.undsf.mcmodmgr.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.undsf.mcmodmgr.curseforge.CurseForgeApiClient
import com.undsf.mcmodmgr.util.JsonTemplate
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CurseForgeConfiguration {
    @Autowired
    lateinit var httpClient: OkHttpClient

    @Autowired
    lateinit var jsonTpl: JsonTemplate

    @Value("\${curse-forge.base-url:https://api.curseforge.com}")
    lateinit var baseUrl: String

    @Value("\${curse-forge.api-key:}")
    lateinit var apiKey: String

    @Bean("curse-forge-api-client")
    fun getCurseForgeApiClient() : CurseForgeApiClient {
        return CurseForgeApiClient(httpClient, apiKey, baseUrl)
    }
}
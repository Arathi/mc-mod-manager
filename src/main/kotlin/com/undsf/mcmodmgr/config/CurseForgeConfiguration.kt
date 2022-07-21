package com.undsf.mcmodmgr.config

import com.undsf.mcmodmgr.curseforge.CurseForgeApiClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CurseForgeConfiguration {
    @Value("\${curse-forge.api-key:}")
    lateinit var apiKey: String

    @Value("\${curse-forge.base-url:https://api.curseforge.com}")
    lateinit var baseUrl: String

    @Bean("curse-forge-api-client")
    fun getCurseForgeApiClient() : CurseForgeApiClient {
        return CurseForgeApiClient(apiKey, baseUrl)
    }
}
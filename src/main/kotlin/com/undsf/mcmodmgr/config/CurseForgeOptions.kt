package com.undsf.mcmodmgr.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "curse-forge")
class CurseForgeOptions {
    /**
     * API地址
     */
    var baseUrl: String = "https://api.curseforge.com"

    /**
     * API_KEY
     */
    var apiKey: String = ""

    /**
     * 索引缓存目录
     */
    var indexesPath: String = ""

    /**
     * MOD下载目录
     */
    var modsPath: String = ""
}
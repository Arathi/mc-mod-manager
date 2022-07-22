package com.undsf.mcmodmgr.config

import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.InetSocketAddress
import java.net.Proxy

@Configuration
class OkHttpConfiguration {
    @Value("\${okhttp.proxy.type:DIRECT}")
    lateinit var proxyType: String

    @Value("\${okhttp.proxy.host:127.0.0.1}")
    lateinit var proxyHost: String

    @Value("\${okhttp.proxy.port:1089}")
    var proxyPort: Int = 0

    @Bean("okhttp-client")
    fun getOkHttpClient() : OkHttpClient {
        val builder = OkHttpClient.Builder()

        val proxy: Proxy? = getProxy()
        if (proxy != null) {
            builder.proxy(proxy)
        }

        return builder.build()
    }

    private fun getProxy() : Proxy? {
        val type = when (proxyType.uppercase()) {
            "", "DIRECT", "NONE", "NO_PROXY" -> null
            "HTTP" -> Proxy.Type.HTTP
            "SOCK", "SOCK4", "SOCKS5" -> Proxy.Type.SOCKS
            else -> null
        } ?: return null

        val addr = InetSocketAddress.createUnresolved(proxyHost, proxyPort)
        return Proxy(type, addr)
    }
}
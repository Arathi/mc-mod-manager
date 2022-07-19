package com.undsf.mcmodmgr.config;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

@Configuration
@Slf4j
public class OkHttpConfiguration {
    @Value("${mmm.http.proxy.type:}")
    public String proxyType;

    @Value("${mmm.http.proxy.host:127.0.0.1}")
    public String proxyHost;

    @Value("${mmm.http.proxy.port:1080}")
    public int proxyPort;

    @Bean("ok-http-client")
    public OkHttpClient getHttpClient() {
        var builder = new OkHttpClient.Builder();

        if (proxyType != null && !proxyType.isEmpty()) {
            Proxy.Type pt = Proxy.Type.SOCKS;
            log.info("使用代理服务器：{}:{}", proxyHost, proxyPort);
            SocketAddress proxyAddr = new InetSocketAddress(proxyHost, proxyPort);
            Proxy proxy = new Proxy(pt, proxyAddr);
            builder.proxy(proxy);
        }
        else {
            log.info("不使用代理服务器");
        }

        return builder.build();
    }
}

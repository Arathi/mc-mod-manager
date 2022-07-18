package com.undsf.mcmodmgr.config;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

public class OkHttpConfiguration {
    @Value("${mmm.http.proxy.type:SOCKS5}")
    public String proxyType;

    @Value("${mmm.http.proxy.host:127.0.0.1}")
    public String proxyHost;

    @Value("${mmm.http.proxy.port:41080}")
    public int proxyPort;

    @Bean("ok-http-client")
    public OkHttpClient getHttpClient() {
        var builder = new OkHttpClient.Builder();

        if (proxyType != null && !proxyType.isEmpty()) {
            SocketAddress proxyAddr = new InetSocketAddress(proxyHost, proxyPort);
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, proxyAddr);
            builder.proxy(proxy);
        }

        return builder.build();
    }
}

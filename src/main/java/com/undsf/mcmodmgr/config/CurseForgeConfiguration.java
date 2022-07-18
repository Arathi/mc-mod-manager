package com.undsf.mcmodmgr.config;

import com.undsf.mcmodmgr.curseforge.CurseForgeApi;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CurseForgeConfiguration {
    @Value("${mmm.curse-forge.api-key}")
    public String apiKey;

    @Autowired
    public OkHttpClient httpClient;

    @Bean("curse-forge-api")
    public CurseForgeApi getCurseForgeApi() {
        return new CurseForgeApi(httpClient, apiKey);
    }
}

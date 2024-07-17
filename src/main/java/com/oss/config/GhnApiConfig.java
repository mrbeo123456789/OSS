package com.oss.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GhnApiConfig {

    @Value("${ghn.api.url}")
    private String apiUrl;

    @Value("${ghn.api.key}")
    private String apiKey;

    public String getApiUrl() {
        return apiUrl;
    }

    public String getApiKey() {
        return apiKey;
    }
}

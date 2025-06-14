package com.oss.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/Avt/**")
                .addResourceLocations("file:./Avt/");
        registry.addResourceHandler("/image/product/**")
                .addResourceLocations("file:./image/product/");
    }
}

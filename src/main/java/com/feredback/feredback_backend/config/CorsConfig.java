package com.feredback.feredback_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: Online-Marking-Backend
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-07-05 01:58
 **/
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(".**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods("GET","POST","PUT","DELETE")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}

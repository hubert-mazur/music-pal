package com.hm.zti.fis.musicpal.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("https://musicpal-fe.herokuapp.com").allowedHeaders("Content-Type", "auth-token").allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE");
            }
        };
    }
}
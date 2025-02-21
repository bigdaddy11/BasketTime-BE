package com.prod.main.baskettime;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
    @Value("${cors.allowed-origins}")
    private String allowedOrigins;  // YML에서 설정된 값 주입
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        System.out.println("allowedOrigins : " + allowedOrigins);
        List<String> origins = Arrays.asList(allowedOrigins.split(",")); // 문자열을 리스트로 변환
        registry.addMapping("/**")  // 모든 경로에 대해 CORS 허용
                //.allowedOrigins(origins.toArray(new String[0]))  // 요청을 허용할 도메인 (React Native 앱 실행 도메인 추가)
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

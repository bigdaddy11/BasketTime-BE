package com.prod.main.baskettime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()   // CSRF가 필요 없을 경우 비활성화
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/**","/oauth2/**").permitAll()  // Replace .antMatchers with .requestMatchers
                .anyRequest().authenticated()
            )
            .oauth2Login()
                .defaultSuccessUrl("/api/auth/google/callback", true);  
        return http.build();
    }
}

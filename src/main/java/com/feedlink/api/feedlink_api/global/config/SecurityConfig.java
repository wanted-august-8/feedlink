package com.feedlink.api.feedlink_api.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
                    "/api/members/signup").permitAll() // Swagger 관련 경로 허용
                .anyRequest().authenticated() // 나머지 모든 요청은 인증 필요
            )
            .csrf(csrf -> csrf.disable()) // 전체 CSRF 비활성화
            .formLogin(formLogin -> formLogin.disable())  // 폼 로그인을 비활성화
            .httpBasic(httpBasic -> httpBasic.disable());  // HTTP Basic 인증을 비활성화

        return  http.build();
    }
        @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
package com.feedlink.api.feedlink_api.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedlink.api.feedlink_api.global.security.autheniation.LoginAuthenticationConfigurer;
import com.feedlink.api.feedlink_api.global.security.autheniation.LoginAuthenticationFailureHandler;
import com.feedlink.api.feedlink_api.global.security.autheniation.LoginAuthenticationFilter;
import com.feedlink.api.feedlink_api.global.security.autheniation.LoginAuthenticationSuccessHandler;
import com.feedlink.api.feedlink_api.global.security.autheniation.LogoutTokenHandler;
import com.feedlink.api.feedlink_api.global.security.autheniation.LogoutTokenSuccessHandler;
import com.feedlink.api.feedlink_api.global.security.autheniation.token.TokenService;
import com.feedlink.api.feedlink_api.global.security.authorization.SecurityAccessDeniedHandler;
import com.feedlink.api.feedlink_api.global.security.authorization.SecurityAuthenticationEntryPoint;
import com.feedlink.api.feedlink_api.global.security.authorization.TokenAuthorityConfigurer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/env",
                    "/hc", "/api/members/signup", "/api/members/login", "/api/members/logout",
                    "/api/security/reissue", "/api/members/verify")
                .permitAll() // Swagger, Health Check 관련 경로 허용
                .anyRequest().authenticated() // 나머지 모든 요청은 인증 필요
            )
            .csrf(csrf -> csrf.disable()) // 전체 CSRF 비활성화
            .formLogin(formLogin -> formLogin.disable())  // 폼 로그인을 비활성화
            .httpBasic(httpBasic -> httpBasic.disable())  // HTTP Basic 인증을 비활성화
            .logout(
                logoutConfigurer -> logoutConfigurer
                    .logoutUrl("/api/members/logout")
                    .addLogoutHandler(createLogoutHandler())
                    .logoutSuccessHandler(createLogoutSuccessHandler())
            )
            .with(
                new LoginAuthenticationConfigurer<>(createAuthenticationFilter()),
                SecurityAuthenticationFilter -> SecurityAuthenticationFilter
                    .successHandler(createAuthenticationSuccessHandler())
                    .failureHandler(createAuthenticationFailureHandler()))
            .with(
                new TokenAuthorityConfigurer(tokenService, userDetailsService),
                Customizer.withDefaults()
            )
            .exceptionHandling(
                exceptionHandling -> exceptionHandling
                    .accessDeniedHandler(createAccessDeniedHandler())
                    .authenticationEntryPoint(createAuthenticationEntryPoint())
            );

        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private LogoutHandler createLogoutHandler() {
        return new LogoutTokenHandler(objectMapper, tokenService);
    }

    private LogoutSuccessHandler createLogoutSuccessHandler() {
        return new LogoutTokenSuccessHandler(objectMapper);
    }

    private AbstractAuthenticationProcessingFilter createAuthenticationFilter() {
        return new LoginAuthenticationFilter(objectMapper);
    }

    private AuthenticationSuccessHandler createAuthenticationSuccessHandler() {
        return new LoginAuthenticationSuccessHandler(objectMapper, tokenService);
    }

    private AuthenticationFailureHandler createAuthenticationFailureHandler() {
        return new LoginAuthenticationFailureHandler(objectMapper);
    }

    private AccessDeniedHandler createAccessDeniedHandler() {
        return new SecurityAccessDeniedHandler();
    }

    private AuthenticationEntryPoint createAuthenticationEntryPoint() {
        return new SecurityAuthenticationEntryPoint(objectMapper);
    }
}

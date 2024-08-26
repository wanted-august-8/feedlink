package com.feedlink.api.feedlink_api.global.security.autheniation;

import static com.feedlink.api.feedlink_api.global.error.ErrorCode.EMPTY_FAILURE_HANDLER;
import static com.feedlink.api.feedlink_api.global.error.ErrorCode.EMPTY_SUCCESS_HANDLER;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class LoginAuthenticationConfigurer <T extends AbstractAuthenticationProcessingFilter>
    extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final T authenticationFilter;

    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;

    @Override
    public void init(HttpSecurity builder) {
    }

    /**
     * HttpSecurity builder를 사용해 커스텀 인증 필터를 구성하는 메서드입니다.
     *
     * @param builder   인증 필터가 추가될 HttpSecurity
     * */
    @Override
    public void configure(HttpSecurity builder) {
        validateHandler(); // 성공 및 실패 핸들러가 올바르게 설정되었는지 확인
        setAuthenticationFilter(builder);
        builder.addFilterBefore(
            authenticationFilter,
            UsernamePasswordAuthenticationFilter.class
        ); //UsernamePasswordAuthenticationFilter` 앞에 추가되어 Spring Security의 인증 처리 과정에서 우선적으로 동작
    }

    public LoginAuthenticationConfigurer<T> successHandler(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
        return this;
    }

    public LoginAuthenticationConfigurer<T> failureHandler(AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
        return this;
    }

    private void validateHandler() {
        if (successHandler == null) {
            throw new IllegalStateException(EMPTY_SUCCESS_HANDLER.getMessage());
        }

        if (failureHandler == null) {
            throw new IllegalStateException(EMPTY_FAILURE_HANDLER.getMessage());
        }
    }

    private void setAuthenticationFilter(HttpSecurity builder) {

        //`HttpSecurity` 빌더로부터 공유된 `AuthenticationManager` 객체를 가져와 `authenticationFilter`에 설정합니다.
        authenticationFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        authenticationFilter.setAuthenticationSuccessHandler(successHandler);
        authenticationFilter.setAuthenticationFailureHandler(failureHandler);
    }
}
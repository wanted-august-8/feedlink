package com.feedlink.api.feedlink_api.global.security.autheniation;

import static com.feedlink.api.feedlink_api.global.error.ErrorCode.INVALID_INPUT_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedlink.api.feedlink_api.domain.member.dto.MemberLoginRequest;
import com.feedlink.api.feedlink_api.global.security.exception.InvalidAuthenticationArgumentException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
        new AntPathRequestMatcher("/api/members/login", "POST");

    private final ObjectMapper objectMapper;

    public LoginAuthenticationFilter(ObjectMapper objectMapper) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws AuthenticationException, InvalidAuthenticationArgumentException {
        MemberLoginRequest loginRequest = getLoginInfo(request);
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        log.info("Login email = {} / password = {}", email, password);

        UsernamePasswordAuthenticationToken authRequest =
            UsernamePasswordAuthenticationToken.unauthenticated(email, password);
        return getAuthenticationManager().authenticate(authRequest);
    }

    private MemberLoginRequest getLoginInfo(HttpServletRequest request)
        throws InvalidAuthenticationArgumentException {
        try {
            return objectMapper.readValue(request.getReader(), MemberLoginRequest.class);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new InvalidAuthenticationArgumentException(INVALID_INPUT_VALUE);
        }
    }
}

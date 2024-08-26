package com.feedlink.api.feedlink_api.global.security.autheniation;

import static com.feedlink.api.feedlink_api.global.error.ErrorCode.LOGIN_FAIL;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedlink.api.feedlink_api.global.common.CommonResponse;
import com.feedlink.api.feedlink_api.global.security.exception.InvalidAuthenticationArgumentException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
@Slf4j
@RequiredArgsConstructor
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {


    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception
    ) throws IOException {
        HttpStatus httpStatus;
        CommonResponse<Void> commonResponse;

        if (exception instanceof InvalidAuthenticationArgumentException) {
            httpStatus = HttpStatus.BAD_REQUEST;
            commonResponse = CommonResponse.fail(exception.getMessage());
        } else {
            httpStatus = HttpStatus.UNAUTHORIZED;
            if (exception instanceof BadCredentialsException) {
                commonResponse = CommonResponse.fail(LOGIN_FAIL.getMessage());
            } else {
                commonResponse = CommonResponse.fail(exception.getMessage());
            }
        }

        response.setStatus(httpStatus.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        objectMapper.writeValue(response.getWriter(), commonResponse);
    }
}


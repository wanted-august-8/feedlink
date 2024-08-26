package com.feedlink.api.feedlink_api.global.security.autheniation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedlink.api.feedlink_api.domain.member.dto.MemberLogoutRequest;
import com.feedlink.api.feedlink_api.global.error.ErrorCode;
import com.feedlink.api.feedlink_api.global.exception.CustomException;
import com.feedlink.api.feedlink_api.global.security.autheniation.token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
@Slf4j
@RequiredArgsConstructor
public class LogoutTokenHandler implements LogoutHandler {

    private final ObjectMapper objectMapper;
    private final TokenService tokenService;

    @Override
    public void logout(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) {
        MemberLogoutRequest logoutRequest = getLogoutInfo(request);
        log.info("Logout Info = {}", logoutRequest);

        tokenService.removeRefreshToken(logoutRequest.getRefreshToken());
        tokenService.addBlackList(logoutRequest.getAccessToken());
    }

    private MemberLogoutRequest getLogoutInfo(HttpServletRequest request) {
        try {
            return objectMapper.readValue(request.getReader(), MemberLogoutRequest.class);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}


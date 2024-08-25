package com.feedlink.api.feedlink_api.global.security.autheniation.dto;

import static com.feedlink.api.feedlink_api.global.error.ErrorCode.EMPTY_ACCESS_TOKEN;
import static com.feedlink.api.feedlink_api.global.error.ErrorCode.EMPTY_REFRESH_TOKEN;

import com.feedlink.api.feedlink_api.global.exception.CustomException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
@Getter
@NoArgsConstructor
public class LogoutRequest {
    private String refreshToken;
    private String accessToken;

    public LogoutRequest(String refreshToken, String accessToken) {
        if (!StringUtils.hasText(refreshToken)) {
            throw new CustomException(EMPTY_REFRESH_TOKEN);
        }
        if (!StringUtils.hasText(accessToken)) {
            throw new CustomException(EMPTY_ACCESS_TOKEN);
        }

        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }
}

package com.feedlink.api.feedlink_api.global.security.autheniation.dto;

import com.feedlink.api.feedlink_api.global.security.autheniation.token.dto.Token;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthenticationToken {

    private String grantType;

    private String refreshToken;

    private Long expiresIn;

    private String accessToken;
    public static AuthenticationToken of(Token token) {
        return AuthenticationToken.builder()
            .grantType(token.getGrantType())
            .refreshToken(token.getRefreshToken())
            .accessToken(token.getAccessToken())
            .expiresIn(token.getAccessTokenExpired())
            .build();
    }
}



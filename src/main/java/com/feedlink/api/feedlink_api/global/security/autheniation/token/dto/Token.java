package com.feedlink.api.feedlink_api.global.security.autheniation.token.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    private String grantType;

    private String refreshToken;

    private Long refreshTokenExpired;

    private String accessToken;

    private Long accessTokenExpired;

}


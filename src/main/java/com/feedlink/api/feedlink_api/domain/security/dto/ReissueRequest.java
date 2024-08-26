package com.feedlink.api.feedlink_api.domain.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class ReissueRequest {

    @NotBlank(message = "refreshToken는 필수 값 입니다.")
    private String refreshToken;

    @NotBlank(message = "accessToken는 필수 값 입니다.")
    private String accessToken;
}

package com.feedlink.api.feedlink_api.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberLogoutRequest {

    @Schema(description = "refreshToken", example = "asdkfjaasdf3123409123...")
    @NotBlank(message = "refreshToken는 필수 값 입니다.")
    private String refreshToken;

    @Schema(description = "accessToken", example = "asdkfjaasdf3123409123...")
    @NotBlank(message = "accessToken는 필수 값 입니다.")
    private String accessToken;
}

package com.feedlink.api.feedlink_api.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberVerificationRequest {
    @Schema(description = "계정(아이디)", example = "peaceB")
    @NotBlank(message = "계정은 필수 입력 값입니다.")
    private String memberAccount;

    @Schema(description = "비밀번호", example = "Password123!!")
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String memberPwd;

    @Schema(description = "인증코드", example = "345674")
    @NotBlank(message = "인증 코드는 필수 입력 값입니다.")
    @Size(min = 6, max = 6, message = "인증 코드는 6자리여야 합니다.")
    private String memberCode;
}

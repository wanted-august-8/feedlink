package com.feedlink.api.feedlink_api.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberVerificationRequest {
    @NotBlank(message = "계정은 필수 입력 값입니다.")
    private String memberAccount;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String memberPwd;

    @NotBlank(message = "인증 코드는 필수 입력 값입니다.")
    @Size(min = 6, max = 6, message = "인증 코드는 6자리여야 합니다.")
    private String memberCode;
}

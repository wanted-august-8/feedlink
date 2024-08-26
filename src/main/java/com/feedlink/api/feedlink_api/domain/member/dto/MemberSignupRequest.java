package com.feedlink.api.feedlink_api.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class MemberSignupRequest {
    @Schema(description = "이메일", example = "peaceB@domain.com")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String memberEmail;

    @Schema(description = "비밀번", example = "Password123!!")
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 10, message = "비밀번호는 최소 10자 이상이어야 합니다.")
    private String memberPwd;

    @Schema(description = "계정(아이디)", example = "peaceB")
    @NotBlank(message = "계정은 필수 입력 값입니다.")
    @Size(min = 4, max = 20, message = "계정은 4자 이상, 20자 이하로 입력해야 합니다.")
    private String memberAccount;
}

package com.feedlink.api.feedlink_api.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberSignupRequest {
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String memberEmail;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 10, message = "비밀번호는 최소 10자 이상이어야 합니다.")
    private String memberPwd;

    @NotBlank(message = "계정은 필수 입력 값입니다.")
    @Size(min = 4, max = 20, message = "계정은 4자 이상, 20자 이하로 입력해야 합니다.")
    private String memberAccount;
}

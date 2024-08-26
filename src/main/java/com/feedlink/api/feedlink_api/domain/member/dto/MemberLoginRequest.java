package com.feedlink.api.feedlink_api.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberLoginRequest {

    @Schema(description = "이메일", example = "abc123@gmail.com")
    @NotBlank(message = "이메일은 필수 입력 값 입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Schema(description = "비밀번호", example = "abc12345678")
    @NotBlank(message = "이메일은 필수 입력 값 입니다.")
    @Size(min = 10, message = "비밀번호는 최소 10자 이상이어야 합니다.")
    private String password;
}

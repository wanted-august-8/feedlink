package com.feedlink.api.feedlink_api.domain.member.controller;

import com.feedlink.api.feedlink_api.domain.member.dto.MemberLoginRequest;
import com.feedlink.api.feedlink_api.domain.member.dto.MemberSignupRequest;
import com.feedlink.api.feedlink_api.domain.member.dto.MemberVerificationRequest;
import com.feedlink.api.feedlink_api.domain.member.service.MemberService;
import com.feedlink.api.feedlink_api.domain.security.PrincipalDetails;
import com.feedlink.api.feedlink_api.global.common.CommonResponse;
import com.feedlink.api.feedlink_api.global.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@AllArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입 요청을 처리하는 엔드포인트입니다.
     * 회원가입 요청을 검증하고, 회원가입 프로세스를 실행합니다.
     *
     * @param request 회원가입 요청을 담은 DTO 객체
     * @return 회원가입 결과를 담은 CommonResponse 객체와 HTTP 상태 코드
     */
    @Operation(summary = "회원가입 요청", description = "사용자가 이메일, 비밀번호, 계정 정보를 제공하여 회원가입을 요청합니다.")
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<String>> signup(@Valid @RequestBody MemberSignupRequest request) {
        CommonResponse<String> response = memberService.signup(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 회원가입 승인 요청을 처리하는 엔드포인트입니다.
     * 사용자가 입력한 계정 정보와 인증 코드를 검증하고, 계정을 활성화합니다.
     *
     * @param request 회원가입 승인 요청을 담은 DTO 객체
     * @return 계정 활성화 결과를 담은 CommonResponse 객체와 HTTP 상태 코드
     */
    @Operation(summary = "회원가입 승인 요청", description = "사용자가 인증 코드를 통해 계정을 활성화합니다.")
    @PostMapping("/verify")
    public ResponseEntity<CommonResponse<String>> verifyMember(@Valid @RequestBody MemberVerificationRequest request) {
        CommonResponse<String> response = memberService.verifyMember(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 로그인 요청을 처리하는 엔드포인트입니다.
     *
     * @param request   이메일,비밀번호를 담은 DTO 객체
     */
    @Operation(summary = "로그인", description = "사용자가 이메일, 비밀번호 제공하여 로그인을 요청합니다.")
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<?>> login(@Valid @RequestBody MemberLoginRequest request){

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * @AuthenticationPrincipal accessToken으로 Member 개체를 확인합니다.
     */
    @PostMapping("/test")
    public ResponseEntity<CommonResponse<?>> test(@AuthenticationPrincipal PrincipalDetails member){
        log.info("member check : {}",member.getMember().getMemberEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
package com.feedlink.api.feedlink_api.domain.member.controller;

import com.feedlink.api.feedlink_api.domain.member.dto.MemberSignupRequest;
import com.feedlink.api.feedlink_api.domain.member.service.MemberService;
import com.feedlink.api.feedlink_api.global.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입 요청을 처리하는 엔드포인트입니다.
     * 회원가입 요청을 검증하고, 회원가입 프로세스를 실행합니다.
     *
     * @param request 회원가입 요청을 담은 DTO 객체
     * @return 회원가입 결과를 담은 CommonResponse 객체와 HTTP 상태 코드
     */
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<String>> signup(@Valid @RequestBody MemberSignupRequest request) {
        CommonResponse<String> response = memberService.signup(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
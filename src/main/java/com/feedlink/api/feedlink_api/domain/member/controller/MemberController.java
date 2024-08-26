package com.feedlink.api.feedlink_api.domain.member.controller;

import com.feedlink.api.feedlink_api.domain.member.dto.MemberLoginRequest;
import com.feedlink.api.feedlink_api.domain.member.dto.MemberLogoutRequest;
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
    @Operation(
        summary = "회원가입 요청",
        description = "사용자가 이메일, 비밀번호, 계정 정보를 제공하여 회원가입을 요청합니다.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MemberSignupRequest.class),
                examples = @ExampleObject(
                    value = """
                {
                  "memberEmail": "example@domain.com",
                  "memberPwd": "Password123!",
                  "memberAccount": "exampleUser"
                }
                """
                )
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "회원가입 완료.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CommonResponse.class),
                examples = @ExampleObject(
                    value = """
                {
                  "httpStatus": "CREATED",
                  "message": "회원가입이 성공적으로 완료되었습니다.",
                  "data": null
                }
                """
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "중복된 이메일이나 계정을 입력한 경우",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "이미 존재하는 이메일인 경우",
                        summary = "EMAIL_ALREADY_EXISTS",
                        value = """
                    {
                      "errorCode": "EMAIL_ALREADY_EXISTS",
                      "statusCode": 409,
                      "httpStatus": "CONFLICT",
                      "message": "이미 존재하는 이메일입니다."
                    }
                    """
                    ),
                    @ExampleObject(
                        name = "이미 존재하는 계정인 경우",
                        summary = "ACCOUNT_ALREADY_EXISTS",
                        value = """
                    {
                      "errorCode": "ACCOUNT_ALREADY_EXISTS",
                      "statusCode": 409,
                      "httpStatus": "CONFLICT",
                      "message": "이미 존재하는 계정입니다."
                    }
                    """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "정책에 맞지않는 입력 값을 받은 경우",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "비밀번호 정책에 맞지 않는 경우",
                        summary = "PASSWORD_VIOLATES_POLICY",
                        value = """
                    {
                      "errorCode": "PASSWORD_VIOLATES_POLICY",
                      "statusCode": 400,
                      "httpStatus": "BAD_REQUEST",
                      "message": "비밀번호 정책에 맞지 않습니다."
                    }
                    """
                    ),
                    @ExampleObject(
                        name = "비밀번호가 10자 미만인 경우",
                        summary = "PASSWORD_TOO_SHORT",
                        value = """
                    {
                      "errorCode": "PASSWORD_TOO_SHORT",
                      "statusCode": 400,
                      "httpStatus": "BAD_REQUEST",
                      "message": "비밀번호는 최소 10자 이상이어야 합니다."
                    }
                    """
                    ),
                    @ExampleObject(
                        name = "통상적으로 자주 사용되는 비밀번호인 경우",
                        summary = "PASSWORD_TOO_COMMON",
                        value = """
                    {
                      "errorCode": "PASSWORD_TOO_COMMON",
                      "statusCode": 400,
                      "httpStatus": "BAD_REQUEST",
                      "message": "통상적으로 자주 사용되는 비밀번호는 사용할 수 없습니다."
                    }
                    """
                    ),
                    @ExampleObject(
                        name = "숫자, 문자, 특수문자 중 2가지 이상을 포함하지 않은 경우",
                        summary = "PASSWORD_LACKS_VARIETY",
                        value = """
                    {
                      "errorCode": "PASSWORD_LACKS_VARIETY",
                      "statusCode": 400,
                      "httpStatus": "BAD_REQUEST",
                      "message": "숫자, 문자, 특수문자 중 2가지 이상을 포함해야 합니다.."
                    }
                    """
                    ),
                    @ExampleObject(
                        name = "비밀번호가 다른 개인 정보와 유사할 경우",
                        summary = "PASSWORD_SIMILAR_TO_PERSONAL_INFO",
                        value = """
                    {
                      "errorCode": "PASSWORD_SIMILAR_TO_PERSONAL_INFO",
                      "statusCode": 400,
                      "httpStatus": "BAD_REQUEST",
                      "message": "비밀번호는 다른 개인 정보와 유사할 수 없습니다."
                    }
                    """
                    ),
                    @ExampleObject(
                        name = "3회 이상 연속되는 문자는 사용한 경우",
                        summary = "PASSWORD_HAS_SEQUENTIAL_CHARS",
                        value = """
                    {
                      "errorCode": "PASSWORD_HAS_SEQUENTIAL_CHARS",
                      "statusCode": 400,
                      "httpStatus": "BAD_REQUEST",
                      "message": "3회 이상 연속되는 문자는 사용할 수 없습니다."
                    }
                    """
                    ),
                }
            )
        )
    })
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
    @Operation(
        summary = "회원가입 승인 요청",
        description = "사용자가 인증 코드를 통해 계정을 활성화합니다.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MemberVerificationRequest.class),
                examples = @ExampleObject(
                    value = """
                {
                  "memberAccount": "exampleUser",
                  "memberPwd": "Password123!",
                  "memberCode": "516449"
                }
                """
                )
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "계정이 성공적으로 활성화된 경우",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CommonResponse.class),
                examples = @ExampleObject(
                    value = """
                {
                  "httpStatus": "OK",
                  "message": "계정이 성공적으로 활성화되었습니다.",
                  "data": null
                }
                """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "적절하지 않은 값을 입력 받은 경우",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "비밀번호가 틀린 경우",
                        summary = "PASSWORD_INVALID",
                        value = """
                    {
                      "errorCode": "PASSWORD_INVALID",
                      "statusCode": 400,
                      "httpStatus": "BAD_REQUEST",
                      "message": "비밀번호가 올바르지 않습니다."
                    }
                    """
                    ),
                    @ExampleObject(
                        name = "인증코드가 틀린 경우",
                        summary = "INVALID_MEMBER_CODE",
                        value = """
                    {
                      "errorCode": "INVALID_MEMBER_CODE",
                      "statusCode": 400,
                      "httpStatus": "BAD_REQUEST",
                      "message": "인증 코드가 올바르지 않습니다."
                    }"""
                    ),
                    @ExampleObject(
                        name = "계정이 존재하지 않는 경우",
                        summary = "INVALID_MEMBER_CODE",
                        value = """
                    {
                      "errorCode": "USER_NOT_FOUND",
                      "statusCode": 400,
                      "httpStatus": "BAD_REQUEST",
                      "message": "사용자를 찾을 수 없습니다."
                    }"""
                    )
                }
            )
        )
    })
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
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Request Success"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<?>> login(@Valid @RequestBody MemberLoginRequest request){

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 로그아웃을 처리하는 엔드포인트입니다.
     *
     * @param request   accessToken, refreshToken를 담은 DTO 객체
     */
    @Operation(summary = "로그아웃", description = "사용자가 accessToken, refreshToken을 제공하여 로그아웃을 요청합니다.")
    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<?>> logout(@Valid @RequestBody MemberLogoutRequest request){

        return new ResponseEntity<>(HttpStatus.OK);
    }




}
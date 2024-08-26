package com.feedlink.api.feedlink_api.global.error;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //공통
    INVALID_INPUT_VALUE(BAD_REQUEST, "유효하지 않은 입력 값입니다."),
    COMMON_SYSTEM_ERROR(INTERNAL_SERVER_ERROR, "시스템 오류입니다."),

    //회원가입
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    ACCOUNT_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 계정입니다."),
    PASSWORD_VIOLATES_POLICY(BAD_REQUEST, "비밀번호 정책에 맞지 않습니다."),
    PASSWORD_TOO_SHORT(BAD_REQUEST, "비밀번호는 최소 10자 이상이어야 합니다."),
    PASSWORD_TOO_COMMON(BAD_REQUEST, "통상적으로 자주 사용되는 비밀번호는 사용할 수 없습니다."),
    PASSWORD_LACKS_VARIETY(BAD_REQUEST, "숫자, 문자, 특수문자 중 2가지 이상을 포함해야 합니다."),
    PASSWORD_SIMILAR_TO_PERSONAL_INFO(BAD_REQUEST, "비밀번호는 다른 개인 정보와 유사할 수 없습니다."),
    PASSWORD_SAME_AS_PREVIOUS(BAD_REQUEST, "이전 비밀번호와 동일하게 설정할 수 없습니다."),
    PASSWORD_HAS_SEQUENTIAL_CHARS(BAD_REQUEST, "3회 이상 연속되는 문자는 사용할 수 없습니다."),

    //통계
    DATE_VALIDATE_PARAM(BAD_REQUEST, "날짜 유효성 조건에 맞지 않습니다."),
    VALIDATE_PARAM(BAD_REQUEST, "유효성 조건에 맞지 않습니다."),
    REQUIRED_PARAM(INTERNAL_SERVER_ERROR, "Not Null"),

    //로그인
    USER_NOT_FOUND(BAD_REQUEST, "사용자를 찾을 수 없습니다."),
    EMPTY_SUCCESS_HANDLER(INTERNAL_SERVER_ERROR, "SuccessHandler 필수 값 입니다."),
    EMPTY_FAILURE_HANDLER(INTERNAL_SERVER_ERROR, "FailureHandler 필수 값 입니다."),
    LOGIN_FAIL(BAD_REQUEST, "이메일, 비밀번호를 확인해주세요."),
    INVALID_TOKEN(UNAUTHORIZED, "유효하지 않은 토큰 입니다."),
    SAVE_REFRESH_TOKEN_FAILED(UNAUTHORIZED, "Token 저장 중 오류가 발생 했습니다."),
    EMPTY_REFRESH_TOKEN(BAD_REQUEST, "Refresh Token은 필수 값 입니다."),
    EMPTY_ACCESS_TOKEN(BAD_REQUEST, "Access Token은 필수 값 입니다."),
    LOGOUT_ACCESS_TOKEN(UNAUTHORIZED, "로그아웃 된 토큰입니다."),
    EXPIRED_TOKEN(UNAUTHORIZED, "만료된 토큰 입니다."),
    
    //가입 인증
    PASSWORD_INVALID(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),
    INVALID_MEMBER_CODE(HttpStatus.BAD_REQUEST, "인증 코드가 올바르지 않습니다.")


    ;

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}

package com.feedlink.api.feedlink_api.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다."),
    DATE_VALIDATE_PARAM(HttpStatus.BAD_REQUEST, "날짜 유효성 조건에 맞지 않습니다."),
    VALIDATE_PARAM(HttpStatus.BAD_REQUEST, "유효성 조건에 맞지 않습니다."),
    REQUIED_PARAM(HttpStatus.INTERNAL_SERVER_ERROR, "Not Null");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}

package com.feedlink.api.feedlink_api.global.security.exception;

import com.feedlink.api.feedlink_api.global.error.ErrorCode;
import org.springframework.security.core.AuthenticationException;

public class InvalidJwtException extends AuthenticationException {

    public InvalidJwtException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public InvalidJwtException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
    }
}
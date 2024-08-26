package com.feedlink.api.feedlink_api.global.security.exception;

import com.feedlink.api.feedlink_api.global.error.ErrorCode;
import org.springframework.security.core.AuthenticationException;

public class InvalidAuthenticationArgumentException extends AuthenticationException {
    public InvalidAuthenticationArgumentException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}

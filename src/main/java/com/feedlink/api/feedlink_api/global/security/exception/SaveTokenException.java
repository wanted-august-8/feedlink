package com.feedlink.api.feedlink_api.global.security.exception;

import com.feedlink.api.feedlink_api.global.error.ErrorCode;
import org.springframework.security.core.AuthenticationException;

public class SaveTokenException extends AuthenticationException {

    public SaveTokenException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}


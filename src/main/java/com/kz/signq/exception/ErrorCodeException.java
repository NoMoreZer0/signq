package com.kz.signq.exception;

import lombok.Getter;

@Getter
public class ErrorCodeException extends RuntimeException {
    private final String errorCode;

    public ErrorCodeException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

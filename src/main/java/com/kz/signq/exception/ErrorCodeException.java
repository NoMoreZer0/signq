package com.kz.signq.exception;

import lombok.Getter;

@Getter
public class ErrorCodeException extends Exception {
    private final String errorCode;

    public ErrorCodeException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

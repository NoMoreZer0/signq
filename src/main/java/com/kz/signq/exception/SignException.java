package com.kz.signq.exception;

import lombok.Getter;

@Getter
public class SignException extends ErrorCodeException {
    public SignException(String errorCode, String message) {
        super(errorCode, message);
    }
}

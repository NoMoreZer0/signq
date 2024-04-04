package com.kz.signq.exception;

public class UserNotFoundException extends ErrorCodeException {
    public UserNotFoundException(String errorCode, String message) {
        super(errorCode, message);
    }
}

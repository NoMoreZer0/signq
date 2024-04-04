package com.kz.signq.exception;

public class NoPermissionException extends ErrorCodeException {
    public NoPermissionException(String errorCode, String message) {
        super(errorCode, message);
    }
}

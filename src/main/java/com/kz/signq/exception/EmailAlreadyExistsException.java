package com.kz.signq.exception;

public class EmailAlreadyExistsException extends ErrorCodeException {

    public EmailAlreadyExistsException(String errorCode, String message) {
        super(errorCode, message);
    }
}

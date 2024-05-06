package com.kz.signq.exception;

public class EntityNotFoundException extends ErrorCodeException {
    public EntityNotFoundException(String errorCode, String message) {
        super(errorCode, message);
    }
}

package com.kz.signq.exception;

public class PetitionNotFoundException extends ErrorCodeException {

    public PetitionNotFoundException(String errorCode, String message) {
        super(errorCode, message);
    }
}

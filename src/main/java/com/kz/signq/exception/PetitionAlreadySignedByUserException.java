package com.kz.signq.exception;

public class PetitionAlreadySignedByUserException extends ErrorCodeException {
    public PetitionAlreadySignedByUserException(String errorCode, String message) {
        super(errorCode, message);
    }
}

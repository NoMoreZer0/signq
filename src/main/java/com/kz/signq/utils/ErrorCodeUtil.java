package com.kz.signq.utils;

import com.kz.signq.dto.exception.ExceptionDto;
import com.kz.signq.exception.ErrorCodeException;

public enum ErrorCodeUtil {
    ERR_EMAIL_ALREADY_EXIST,
    ERR_PETITION_NOT_FOUND,
    ERR_PETITION_ALREADY_SIGNED,
    ESP_ERROR_GENERAL_ERROR,
    ESP_ERROR_IIN_NOT_FOUND,
    ESP_PASSWORD_INCORRECT,
    ESP_ERROR_IIN_MISMATCH,
    ESP_ERROR_APPLICATION_ALREADY_SIGNED,
    ESP_ERROR_CERTIFICATE_NOT_EFFECTIVE_YET,
    ESP_ERROR_CERTIFICATE_EXPIRED,
    ESP_ERROR_ISSUER_INVALID,
    ESP_ERROR_CERTIFICATE_NOT_VALID,
    ERR_USER_NOT_FOUND,
    ERR_NO_PERMISSION,
    ERR_ENTITY_NOT_FOUND,
    ERR_SERVER_PROBLEM;

    public static ExceptionDto toExceptionDto(Exception exception) {
        if (exception instanceof ErrorCodeException e) {
            return ExceptionDto.builder()
                    .errorCode(e.getErrorCode())
                    .message(e.getMessage())
                    .build();
        }
        return ExceptionDto.builder()
                .message(exception.getMessage())
                .build();
    }
}

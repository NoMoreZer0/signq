package com.kz.signq.listener;

import com.kz.signq.dto.exception.ExceptionDto;
import com.kz.signq.exception.ErrorCodeException;
import com.kz.signq.utils.ErrorCodeUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ErrorCodeException.class)
    public ResponseEntity<ExceptionDto> handleErrorCodeException(
            ErrorCodeException ex,
            WebRequest request
    ) {
        var dto = ErrorCodeUtil.toExceptionDto(ex);
        return ResponseEntity.badRequest().body(dto);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionDto> handleAuthenticationException (
            AuthenticationException ex,
            WebRequest request
    ) {
        return ResponseEntity.badRequest().body(ErrorCodeUtil.toExceptionDto(ex));
    }

}

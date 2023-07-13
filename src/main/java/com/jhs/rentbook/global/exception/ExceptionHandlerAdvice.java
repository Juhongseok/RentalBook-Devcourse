package com.jhs.rentbook.global.exception;

import com.jhs.rentbook.global.exception.custom.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(BusinessException.class)
    public ExceptionResponse handleBusinessException(BusinessException exception) {
        int statusCode = HttpStatus.BAD_REQUEST.value();
        String message = exception.getMessage();

        return loggingAndSendResponse(statusCode, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse handelMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        int statusCode = HttpStatus.BAD_REQUEST.value();
        String message = exception.getMessage();

        return loggingAndSendResponse(statusCode, message);
    }

    private ExceptionResponse loggingAndSendResponse(int statusCode, String message) {
        log.error("Error Message : {}", message);

        return new ExceptionResponse(statusCode, message);
    }

}

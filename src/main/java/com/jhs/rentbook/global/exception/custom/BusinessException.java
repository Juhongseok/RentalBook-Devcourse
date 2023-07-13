package com.jhs.rentbook.global.exception.custom;

public class BusinessException extends RuntimeException{

    public BusinessException(String message) {
        super(message);
    }
}

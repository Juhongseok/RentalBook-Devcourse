package com.jhs.rentbook.global.exception.custom.filter;

public class AuthorizationException extends RuntimeException{

    public AuthorizationException(String message) {
        super(message);
    }
}

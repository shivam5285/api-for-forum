package com.nowandme.forum.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
    public AuthenticationException(String message, Exception ex) {
        super(message, ex);
    }
    public AuthenticationException(Exception ex) {
        super(ex);
    }
}

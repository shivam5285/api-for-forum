package com.nowandme.forum.model.dao;

public class MaliciousRequestException extends RuntimeException {
    public MaliciousRequestException(String message) {
        super(message);
    }
    public MaliciousRequestException(String message, Exception ex) {
        super(message, ex);
    }
    public MaliciousRequestException(Exception ex) {
        super(ex);
    }
}

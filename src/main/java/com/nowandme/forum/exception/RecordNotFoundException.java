package com.nowandme.forum.exception;

public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(String message) {
        super(message);
    }
    public RecordNotFoundException(String message, Exception ex) {
        super(message, ex);
    }
    public RecordNotFoundException(Exception ex) {
        super(ex);
    }
}

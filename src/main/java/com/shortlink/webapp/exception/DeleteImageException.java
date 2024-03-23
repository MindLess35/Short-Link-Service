package com.shortlink.webapp.exception;

public class DeleteImageException extends RuntimeException {
    public DeleteImageException(String message, Exception e) {
        super(message, e);
    }
    public DeleteImageException(String message) {
        super(message);
    }
}

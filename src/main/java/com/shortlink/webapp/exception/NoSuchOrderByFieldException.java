package com.shortlink.webapp.exception;

public class NoSuchOrderByFieldException extends RuntimeException {
    public NoSuchOrderByFieldException(String messages) {
        super(messages);
    }
}

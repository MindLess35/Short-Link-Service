package com.shortlink.webapp.exception;

public class LinkNotExistsException extends RuntimeException {
    public LinkNotExistsException(String message) {
        super(message);
    }
}

package com.shortlink.webapp.exception;

public class OriginalLinkNotExistsException extends RuntimeException {
    public OriginalLinkNotExistsException(String message) {
        super(message);
    }
}

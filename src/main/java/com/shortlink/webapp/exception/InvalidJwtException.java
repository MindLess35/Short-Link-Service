package com.shortlink.webapp.exception;

public class InvalidJwtException extends RuntimeException {
    public InvalidJwtException(String massage) {
        super(massage);
    }
}

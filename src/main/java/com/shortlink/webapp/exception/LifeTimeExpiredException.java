package com.shortlink.webapp.exception;

public class LifeTimeExpiredException extends RuntimeException {
    public LifeTimeExpiredException(String message) {
        super(message);
    }
}

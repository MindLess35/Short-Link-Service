package com.shortlink.webapp.domain.exception.user.password;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}

package com.shortlink.webapp.domain.exception.user.password;

public class PasswordConfirmationException extends RuntimeException {
    public PasswordConfirmationException(String message) {
        super(message);
    }
}

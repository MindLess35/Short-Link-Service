package com.shortlink.webapp.exception.user.password;

public class PasswordConfirmationException extends RuntimeException {
    public PasswordConfirmationException(String message) {
        super(message);
    }
}

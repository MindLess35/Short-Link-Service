package com.shortlink.webapp.exception;

public class MailVerificationException extends RuntimeException {
    public MailVerificationException(String message) {
        super(message);
    }
}

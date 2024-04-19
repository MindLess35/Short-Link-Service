package com.shortlink.webapp.exception.user;

import com.shortlink.webapp.exception.base.BadRequestBaseException;

public class MailVerificationException extends BadRequestBaseException {
    public MailVerificationException(String message) {
        super(message);
    }
}

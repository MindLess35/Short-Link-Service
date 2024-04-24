package com.shortlink.webapp.domain.exception.user;

import com.shortlink.webapp.domain.exception.base.BadRequestBaseException;

public class MailVerificationException extends BadRequestBaseException {
    public MailVerificationException(String message) {
        super(message);
    }
}

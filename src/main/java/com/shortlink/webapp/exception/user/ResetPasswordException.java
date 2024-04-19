package com.shortlink.webapp.exception.user;

import com.shortlink.webapp.exception.base.BadRequestBaseException;

public class ResetPasswordException extends BadRequestBaseException {
    public ResetPasswordException(String message) {
        super(message);
    }
}

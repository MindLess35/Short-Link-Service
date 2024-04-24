package com.shortlink.webapp.domain.exception.user.password;

import com.shortlink.webapp.domain.exception.base.BadRequestBaseException;

public class ResetPasswordException extends BadRequestBaseException {
    public ResetPasswordException(String message) {
        super(message);
    }
}

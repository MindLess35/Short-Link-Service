package com.shortlink.webapp.exception.user;

import com.shortlink.webapp.exception.base.BadRequestBaseException;

public class InvalidPasswordException extends BadRequestBaseException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}

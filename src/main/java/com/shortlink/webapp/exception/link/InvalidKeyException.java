package com.shortlink.webapp.exception.link;

import com.shortlink.webapp.exception.base.BadRequestBaseException;

public class InvalidKeyException extends BadRequestBaseException {

    public InvalidKeyException(String message) {
        super(message);
    }
}

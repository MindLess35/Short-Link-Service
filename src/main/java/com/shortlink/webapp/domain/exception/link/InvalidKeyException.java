package com.shortlink.webapp.domain.exception.link;

import com.shortlink.webapp.domain.exception.base.BadRequestBaseException;

public class InvalidKeyException extends BadRequestBaseException {

    public InvalidKeyException(String message) {
        super(message);
    }
}

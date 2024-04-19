package com.shortlink.webapp.exception.link;

import com.shortlink.webapp.exception.base.BadRequestBaseException;

public class NoSuchOrderByFieldException extends BadRequestBaseException {
    public NoSuchOrderByFieldException(String messages) {
        super(messages);
    }
}

package com.shortlink.webapp.domain.exception.link;

import com.shortlink.webapp.domain.exception.base.BadRequestBaseException;

public class NoSuchOrderByFieldException extends BadRequestBaseException {
    public NoSuchOrderByFieldException(String messages) {
        super(messages);
    }
}

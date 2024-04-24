package com.shortlink.webapp.domain.exception.user;

import com.shortlink.webapp.domain.exception.base.BadRequestBaseException;

public class ImageOperationException extends BadRequestBaseException {
    public ImageOperationException(String message) {
        super(message);
    }

    public ImageOperationException(Throwable cause) {
        super(cause);
    }

    public ImageOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}

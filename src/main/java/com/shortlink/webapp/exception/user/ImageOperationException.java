package com.shortlink.webapp.exception.user;

import com.shortlink.webapp.exception.base.BadRequestBaseException;

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

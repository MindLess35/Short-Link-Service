package com.shortlink.webapp.exception.link;

import com.shortlink.webapp.exception.base.ResourceNotFoundException;

public class TimeToLiveExpiredException extends ResourceNotFoundException {
    public TimeToLiveExpiredException(String message) {
        super(message);
    }
}

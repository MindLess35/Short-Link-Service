package com.shortlink.webapp.domain.exception.link;

import com.shortlink.webapp.domain.exception.base.ResourceNotFoundException;

public class TimeToLiveExpiredException extends ResourceNotFoundException {
    public TimeToLiveExpiredException(String message) {
        super(message);
    }
}

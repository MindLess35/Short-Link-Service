package com.shortlink.webapp.exception;

public class LinkStatisticsNotExistsException extends RuntimeException {
    public LinkStatisticsNotExistsException(String message) {
        super(message);
    }
}

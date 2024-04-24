package com.shortlink.webapp.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StatTimeUnits {

    BY_HOURS("YYYY-MM-DD\"T\"HH24\"Z\""),

    BY_MINUTES("YYYY-MM-DD\"T\"HH24:MI\"Z\"");

    @Getter
    private final String timeUnits;
}

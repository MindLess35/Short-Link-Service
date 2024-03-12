package com.shortlink.webapp.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StatTimeUnits {

    BY_HOURS("yyyy-mm-dd : HH24"),

    BY_MINUTES("yyyy-mm-dd : HH24-mi");

    @Getter
    private final String timeUnits;
}

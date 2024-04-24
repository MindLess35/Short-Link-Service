package com.shortlink.webapp.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor
public enum TimeUnits {
    MINUTES,//("MINUTES"),
    HOURS,//("HOURS"),
    DAYS;//("DAYS");
//    @JsonCreator
//    private final String timeUnits;
//    @JsonValue
//    public String getTimeUnits(){
//        return timeUnits;
//    }
}

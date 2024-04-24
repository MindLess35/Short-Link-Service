package com.shortlink.webapp.domain.exception.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@SuperBuilder
public class ResponseViolationErrorBody extends BaseResponseErrorBody {

    @Getter
    @Setter
    private Map<String, String> fieldErrors;

}

package com.shortlink.webapp.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;

@UtilityClass
public class LinkUtil {
    public final String APPLICATION_URL = ServletUriComponentsBuilder
                                      .fromCurrentContextPath()
                                      .build()
                                      .toUriString() + "/";

    public String generateRandomString(Short count) {
        return RandomStringUtils.random(count, true, true);
    }

}



package com.shortlink.webapp.dto.response;

import lombok.*;


public interface LinkReadDtoI {
    Long getId();
    String getOriginalLink();
    String getShortLink();

}

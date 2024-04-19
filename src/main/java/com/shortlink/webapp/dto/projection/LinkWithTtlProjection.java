package com.shortlink.webapp.dto.projection;

import java.time.Instant;

public interface LinkWithTtlProjection {

    Long getId();

    String getOriginalLink();

    String getShortLink();

    String getKey();

    Instant getTtl();

}

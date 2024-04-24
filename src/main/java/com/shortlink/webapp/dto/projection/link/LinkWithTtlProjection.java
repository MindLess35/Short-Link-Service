package com.shortlink.webapp.dto.projection.link;

import java.time.Instant;

public interface LinkWithTtlProjection {

    Long getId();

    String getOriginalLink();

    String getShortLink();

    String getKey();

    Instant getTtl();

}

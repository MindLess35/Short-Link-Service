package com.shortlink.webapp.dto.projection.user;

import java.time.Instant;

public interface ResetPasswordWithUserProjection {
    Long getUserId();
    Long getResetId();
    Instant getCreatedAt();
    Instant getResetAt();
}

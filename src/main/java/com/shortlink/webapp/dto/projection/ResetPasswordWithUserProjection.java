package com.shortlink.webapp.dto.projection;

import java.time.Instant;

public interface ResetPasswordWithUserProjection {
    Long getUserId();
    Long getResetId();
    Instant getCreatedAt();
    Instant getResetAt();
}

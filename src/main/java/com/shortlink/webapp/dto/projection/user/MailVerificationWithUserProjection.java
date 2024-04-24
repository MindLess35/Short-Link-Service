package com.shortlink.webapp.dto.projection.user;

import java.time.Instant;
import java.time.LocalDateTime;

public interface MailVerificationWithUserProjection {
    Long getUserId();
    Boolean getVerified();
    Long getVerificationId();
    Instant getCreatedAt();
}

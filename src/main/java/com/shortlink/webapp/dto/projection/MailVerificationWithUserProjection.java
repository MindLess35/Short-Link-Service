package com.shortlink.webapp.dto.projection;

import java.time.LocalDateTime;

public interface MailVerificationWithUserProjection {
    Long getUserId();
    Boolean getVerified();
    Long getVerificationId();
    LocalDateTime getCreatedAt();
}

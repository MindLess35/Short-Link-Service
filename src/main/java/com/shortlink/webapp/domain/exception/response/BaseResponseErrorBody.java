package com.shortlink.webapp.domain.exception.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@SuperBuilder
@RequiredArgsConstructor
public abstract class BaseResponseErrorBody {

    @Builder.Default
    private final Instant timestamp = Instant.now();

    private final int status;

    private final HttpStatus error;

    public int getStatus() {
        return error.value();
    }
}

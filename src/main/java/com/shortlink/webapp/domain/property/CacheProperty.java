package com.shortlink.webapp.domain.property;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "cache")
public class CacheProperty {

    @NotBlank
    private String linkCacheName;

    @NotBlank
    private String redirectCacheName;

    @NotBlank
    private String userCacheName;

    @NotBlank
    private String imageCacheName;

    @Min(1)
    private long linkCacheTtlInSeconds;

    @Min(1)
    private long redirectCacheTtlInSeconds;

    @Min(1)
    private long userCacheTtlInSeconds;

    @Min(1)
    private long imageCacheTtlInSeconds;

}

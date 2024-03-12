package com.shortlink.webapp.property;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "jwt")
public class JwtProperty {

    @NotBlank
    private String secretKey;

    @Min(1)
    @NotNull
    private Long jwtExpiration;

    @Min(1)
    @NotNull
    private Long refreshExpiration;
}

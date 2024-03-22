package com.shortlink.webapp.property;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "token")
public class TokenProperty {

    @Min(1)
    @NotNull
    private byte resetPasswordExpiration;

    @Min(1)
    @NotNull
    private byte verifyEmailExpiration;

}

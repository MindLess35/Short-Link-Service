package com.shortlink.webapp.domain.property;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Properties;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "minio")
public class MinioProperty {

    @NotBlank
    private String url;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String bucket;
}

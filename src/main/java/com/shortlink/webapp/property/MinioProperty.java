package com.shortlink.webapp.property;

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
@NotBlank
@Validated
@ConfigurationProperties(prefix = "minio")
public class MinioProperty {

    private String url;

    private String username;

    private String password;

    private String bucket;
}

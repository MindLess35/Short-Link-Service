package com.shortlink.webapp.config;

import com.shortlink.webapp.domain.property.MinioProperty;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MinioConfig {

    private final MinioProperty minioProperty;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioProperty.getUrl())
                .credentials(minioProperty.getUsername(), minioProperty.getPassword())
                .build();
    }

}

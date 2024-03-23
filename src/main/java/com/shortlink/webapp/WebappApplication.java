package com.shortlink.webapp;

import com.shortlink.webapp.entity.Token;
import com.shortlink.webapp.entity.User;
import com.shortlink.webapp.entity.enums.TokenType;
import com.shortlink.webapp.property.MinioProperty;
import com.shortlink.webapp.repository.TokenRepository;
import com.shortlink.webapp.repository.UserRepository;
import com.shortlink.webapp.service.JwtService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.HibernateHints;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.shortlink.webapp.entity.enums.Role.ADMIN;
import static com.shortlink.webapp.entity.enums.Role.MANAGER;

@EnableAsync
@SpringBootApplication
@ConfigurationPropertiesScan
public class WebappApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebappApplication.class, args);
    }

    @Bean
    public CommandLineRunner forCreateBucketInMinio(MinioClient minioClient,
                                                    MinioProperty minioProperty) {
        return args -> {
            String bucket = minioProperty.getBucket();

            if (!minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucket)
                    .build()))
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucket)
                        .build());
        };
    }

    @Bean
    public CommandLineRunner forCreateAdminInPostgres(
            UserRepository userRepository,
            JwtService jwtService,
            TokenRepository tokenRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            User admin = User.builder()
                    .username("Admin")
                    .email("admin@gmail.com")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("admin"))
                    .role(ADMIN)
                    .build();

            String accessTokenAdmin = jwtService.generateToken(admin);
            if (!userRepository.existsByUsername(admin.getUsername())) {
                User savedAdmin = userRepository.save(admin);
                Token token = Token.builder()
                        .token(accessTokenAdmin)
                        .user(savedAdmin)
                        .tokenType(TokenType.BEARER)
                        .expired(false)
                        .revoked(false)
                        .build();
                tokenRepository.save(token);
            }


            User manager = User.builder()
                    .username("Manager")
                    .email("manager@gmail.com")
                    .password(passwordEncoder.encode("manager"))
                    .role(MANAGER)
                    .build();

            String accessTokenManager = jwtService.generateToken(manager);
            if (!userRepository.existsByUsername(manager.getUsername())) {
                User savedManager = userRepository.save(manager);
                Token token = Token.builder()
                        .token(accessTokenManager)
                        .user(savedManager)
                        .tokenType(TokenType.BEARER)
                        .expired(false)
                        .revoked(false)
                        .build();
                tokenRepository.save(token);
            }

            System.out.println("Admin access token: " + accessTokenAdmin);
            System.out.println("Admin refresh token: " + jwtService.generateRefreshToken(admin));

            System.out.println("Manager access token: " + accessTokenManager);
            System.out.println("Manager refresh token: " + jwtService.generateRefreshToken(manager));

        };
    }

}

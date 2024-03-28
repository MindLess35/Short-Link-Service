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
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.HibernateHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.*;

import static com.shortlink.webapp.entity.enums.Role.ADMIN;
import static com.shortlink.webapp.entity.enums.Role.MANAGER;

@EnableAsync
@SpringBootApplication
@ConfigurationPropertiesScan
public class WebappApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebappApplication.class, args);
//       2007-12-03T10:15:30                        LocalDateTime
//       2007-12-03T10:15:30+01:00                  OffsetDateTime
//       2007-12-03T10:15:30+01:00 Europe/Paris     ZonedDateTime
//        LocalDateTime localDateTime = LocalDateTime
//                .of(2024, 3, 28, 15, 30, 59, 0);
//        System.out.println(localDateTime);
//
//        System.out.println(localDateTime.plusHours(3));
//        System.out.println(localDateTime.plusHours(3).toInstant(ZoneOffset.ofHours(3)));
//        System.out.println(localDateTime.plusHours(3).toInstant(ZoneOffset.ofHours(3)).plus(Duration.ofHours(2)));
//        System.out.println(localDateTime.minusHours(10).toInstant(ZoneOffset.ofHours(-10)));
//        System.out.println(Instant.now());
//        System.out.println("--------------------------------------------------- OffsetDateTime");
//
//        OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.ofHours(3));
//        OffsetDateTime offsetDateTime1 = OffsetDateTime.of(localDateTime, ZoneOffset.ofHours(-3));
//        System.out.println(offsetDateTime);
//        System.out.println(offsetDateTime1);
//        System.out.println("--------------------------------------------------- OffsetDateTime UTC");
//        System.out.println(offsetDateTime.toInstant());
//        System.out.println(offsetDateTime1.toInstant());
//
//        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("Europe/Paris"));
//        System.out.println("--------------------------------------------------- ZonedDateTime");
//        System.out.println(zonedDateTime);
//        System.out.println("--------------------------------------------------- ZonedDateTime UTC");
//        System.out.println(zonedDateTime.toInstant());
//        System.out.println(ZonedDateTime.now());
//
//
//        System.out.println();
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
    public CommandLineRunner forSaveAdminInPostgres(
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

package com.shortlink.webapp.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
//@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperty {
    //    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    //    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    //    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;
}

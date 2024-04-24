package com.shortlink.webapp.service.impl.auth;

import com.shortlink.webapp.domain.property.JwtProperty;
import com.shortlink.webapp.service.interfaces.auth.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtProperty jwtProperty;

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    //public String generateToken(UserDetails userDetails)
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    @Override
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
//            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, jwtProperty.getJwtExpiration());
    }

    @Override
    public String generateRefreshToken(
            UserDetails userDetails
//            UserDetails userDetails
    ) {
        return buildToken(new HashMap<>(), userDetails, jwtProperty.getRefreshExpiration());
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
//            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
//                .addClaims(Map.of("id", userDetails.getId()))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperty.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

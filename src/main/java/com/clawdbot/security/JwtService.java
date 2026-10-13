package com.clawdbot.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtService {
    private final Key key;
    private final String issuer;
    private final Duration expiration;

    public JwtService(
            @Value("${clawdbot.api.jwt-secret:}") String secret,
            @Value("${clawdbot.api.jwt-issuer:clawdbot}") String issuer,
            @Value("${clawdbot.api.jwt-expiration-minutes:120}") long expirationMinutes
    ) {
        if (secret == null || secret.isBlank()) {
            this.key = null;
        } else {
            this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
        this.issuer = issuer;
        this.expiration = Duration.ofMinutes(expirationMinutes);
    }

    public boolean isEnabled() {
        return key != null;
    }

    public Claims parseToken(String token) {
        if (key == null) {
            throw new JwtException("JWT secret ausente.");
        }
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .requireIssuer(issuer)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(String subject) {
        if (key == null) {
            throw new JwtException("JWT secret ausente.");
        }
        Instant now = Instant.now();
        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(expiration)))
                .signWith(key)
                .compact();
    }
}


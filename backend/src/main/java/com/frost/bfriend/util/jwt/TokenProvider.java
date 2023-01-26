package com.frost.bfriend.util.jwt;

import com.frost.bfriend.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static com.frost.bfriend.constants.JwtConstants.*;

@Slf4j
@Component
public class TokenProvider {

    @Value("${jwt.access-token.secretkey}")
    private String ACCESS_TOKEN_SECRET_KEY;


    public String createAccessToken(User user) {
        Key key = Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        Date expiryTime = Date.from(Instant.now().plus(ACCESS_TOKEN_EXPIRY_MINUTES, ChronoUnit.MINUTES));
        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(user.getId().toString())
                .setExpiration(expiryTime)
                .setIssuedAt(new Date())
                .compact();
    }

    public long validateAndGetUserId(String token) {
        return Long.parseLong(Jwts.parserBuilder()
                .setSigningKey(ACCESS_TOKEN_SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }
}

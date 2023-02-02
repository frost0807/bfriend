package com.frost.bfriend.common.util.jwt;

import com.frost.bfriend.entity.User;
import io.jsonwebtoken.Claims;
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
import java.util.HashMap;

import static com.frost.bfriend.common.constants.JwtConstants.*;
import static com.frost.bfriend.dto.UserDto.*;

@Slf4j
@Component
public class TokenProvider {

    @Value("${jwt.access-token.secretkey}")
    private String ACCESS_TOKEN_SECRET_KEY;


    public String createAccessToken(User user) {
        Key key = Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        Date expiryTime = Date.from(Instant.now().plus(ACCESS_TOKEN_EXPIRY_SECONDS, ChronoUnit.MINUTES));
        HashMap<String, String> authClaim = new HashMap<>();
        authClaim.put(USER_ID, String.valueOf(user.getId()));
        authClaim.put(USER_LEVEL, user.getLevel().name());

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(user.getId().toString())
                .setClaims(authClaim)
                .setExpiration(expiryTime)
                .setIssuedAt(new Date())
                .compact();
    }

    public UserIdAndLevel validateAndGetUserIdAndLevel(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(ACCESS_TOKEN_SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return new UserIdAndLevel(claims.get(USER_ID, String.class), claims.get(USER_LEVEL, String.class));
    }
}

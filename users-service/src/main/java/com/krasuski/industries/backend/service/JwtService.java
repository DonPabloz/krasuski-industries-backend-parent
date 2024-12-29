package com.krasuski.industries.backend.service;

import com.krasuski.industries.backend.domain.UserRole;
import com.krasuski.industries.backend.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {

    private static final String USER_ROLE_CLAIM_NAME = "role";

    private final String secretKey;
    private final Integer jwtTimeToLive;

    public JwtService(@Value("${jwt-secret-key}") String secretKey, @Value("${jwt-time-to-live}") Integer jwtTimeToLive) {
        this.secretKey = secretKey;
        this.jwtTimeToLive = jwtTimeToLive;
    }

    public String generateTokenDDD(String userPublicId, UserRole role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_ROLE_CLAIM_NAME, role.toString());

        return createToken(claims, userPublicId);
    }

    public String generateToken(UUID userPublicId, Role role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_ROLE_CLAIM_NAME, role.toString());

        return createToken(claims, userPublicId.toString());
    }

    public boolean isJwtValid(String token) {
        if (StringUtils.isBlank(token)) {
            log.info("JWT is empty");
            return false;
        }

        try {
            extractAllClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("JWT expired");
            return false;
        } catch (Exception e) {
            log.info("JWT invalid");
            return false;
        }
    }

    public UUID extractUserPublicId(String token) {
        try {
            return UUID.fromString(extractClaim(token, Claims::getId));
        } catch (ExpiredJwtException exception) {
            return UUID.fromString(exception.getClaims().getId());
        }
    }

    public String extractUserRole(String token) {
        try {
            return extractClaim(token, claims -> claims.get(USER_ROLE_CLAIM_NAME, String.class));
        } catch (ExpiredJwtException exception) {
            return (String) exception.getClaims().get(USER_ROLE_CLAIM_NAME);
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    private String createToken(Map<String, Object> claims, String userPublicId) {
        Date issuedDate = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(issuedDate.toInstant().toEpochMilli() + jwtTimeToLive);
        return Jwts.builder()
                .setClaims(claims)
                .setId(userPublicId.toString())
                .setIssuedAt(issuedDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}

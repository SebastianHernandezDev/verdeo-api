package com.verdeo.verdeo_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expirationMs;

    // Spring inyecta los valores desde application.properties
    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expirationMs) {

        this.secretKey   = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    // ──────────────────────────────────────────
    // GENERAR TOKEN
    // Guarda userId y role dentro del payload
    // ──────────────────────────────────────────
    public String generateToken(UUID userId, String role) {
        return Jwts.builder()
                .subject(userId.toString())       // "sub" → userId
                .claim("role", role)              // claim custom → role
                .issuedAt(new Date())             // "iat" → cuándo se emitió
                .expiration(new Date(System.currentTimeMillis() + expirationMs)) // "exp"
                .signWith(secretKey)
                .compact();
    }

    // ──────────────────────────────────────────
    // EXTRAER CLAIMS (payload completo)
    // ──────────────────────────────────────────
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // ──────────────────────────────────────────
    // EXTRAER userId
    // ──────────────────────────────────────────
    public UUID extractUserId(String token) {
        return UUID.fromString(extractClaims(token).getSubject());
    }

    // ──────────────────────────────────────────
    // EXTRAER role
    // ──────────────────────────────────────────
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    // ──────────────────────────────────────────
    // VALIDAR TOKEN
    // Devuelve true si la firma es correcta y no expiró
    // ──────────────────────────────────────────
    public boolean isValid(String token) {
        try {
            extractClaims(token); // lanza excepción si algo falla
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
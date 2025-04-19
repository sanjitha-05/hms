package com.app.Hospital.Management.System.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

	private final String SECRET_KEY = "b2f8c3a8e9d4f7a6c1b0e5d9f3a7c2e8"; // Secure 32-character key
    //private final String SECRET_KEY = "your_secure_secret_key"; // Replace with a secure key
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

    private Key getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(Long userId, String email) {
        return Jwts.builder()
                .setSubject(email) // Set the subject (e.g., email)
                .claim("userId", userId) // Add custom claims
                .setIssuedAt(new Date()) // Set the issued date
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Set the expiration time
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Use the signing key
                .compact(); // Build the token
    }
}
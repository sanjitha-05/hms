package com.app.Hospital.Management.System.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

private static final String SECRET = "0F1F2F3F4F5F6F7F8F9FAFBFCFDFEFF0F1F2F3F4F5F6F7F8F9FAFBFCFDFEFF"; // 64 chars = 256 bits
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;

    public String generateToken(Long id,String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 10 hours
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256) 
                .compact();
    }

    public String extractUsername(String token) {
       
        try {
            Claims claims = extractAllClaims(token);
           
            return claims.getSubject();
        } catch (Exception e) {
            System.out.println("Error extracting username: " + e.getMessage()); 
            return null;
        }
      
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, String username) {
        return (extractUsername(token).equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
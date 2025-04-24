// package com.app.Hospital.Management.System.utils;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.JwtParser;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.io.Decoders;
// import io.jsonwebtoken.security.Keys;

// import io.jsonwebtoken.*;
// import io.jsonwebtoken.io.Decoders;
// import io.jsonwebtoken.security.Keys;

// import org.springframework.stereotype.Component;

// import javax.crypto.SecretKey;
// import javax.crypto.spec.SecretKeySpec;
// import java.nio.charset.StandardCharsets;
// import java.security.Key;
// import java.util.Date;

// @Component
// public class JwtUtil {
//     private static final String SECRET = "0F1F2F3F4F5F6F7F8F9FAFBFCFDFEFF0F1F2F3F4F5F6F7F8F9FAFBFCFDFEFF"; // 64 chars = 256 bits
//     private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));

// 	//private final String SECRET_KEY = "b2f8c3a8e9d4f7a6c1b0e5d9f3a7c2e8"; // Secure 32-character key
//     //private final String SECRET_KEY = "your_secure_secret_key"; // Replace with a secure key
//     private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

//     // private Key getSigningKey() {
//     //     byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
//     //     return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
//     // }
//     public String generateToken(String username,long id) {
//         return Jwts.builder()
//                 .setSubject(username)
//                 .claim("userId", id) 
//                 .setIssuedAt(new Date(System.currentTimeMillis()))
//                 .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
//                 .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
//                 .compact();
//     }

//     // public String generateToken(Long userId, String email) {
//     //     return Jwts.builder()
//     //             .setSubject(email) // Set the subject (e.g., email)
//     //             .claim("userId", userId) // Add custom claims
//     //             .setIssuedAt(new Date()) // Set the issued date
//     //             .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Set the expiration time
//     //             .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Use the signing key
//     //             .compact(); // Build the token
//     // }

//     private Key getSigningKey() {
//         byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
//         return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
//     }
//     public String extractUsername(String token) {
//         return extractAllClaims(token).getSubject();
//     }

//     public Date extractExpiration(String token) {
//         return extractAllClaims(token).getExpiration();
//     }

//     private boolean isTokenExpired(String token) {
//         return extractExpiration(token).before(new Date());
//     }

//     public boolean validateToken(String token, String username) {
//         return (extractUsername(token).equals(username) && !isTokenExpired(token));
//     }
//     public Claims extractAllClaims(String token) {
//         return Jwts.parserBuilder()
//                 .setSigningKey(getSigningKey()) // Use the signing key
//                 .build()
//                 .parseClaimsJws(token) // Parse the token
//                 .getBody(); // Extract the claims
//     }

//     // public Claims extractAllClaims(String token) {
//     //     JwtParser parser = Jwts.parser()
//     //             .verifyWith(SECRET_KEY)
//     //             .build();
//     //     return parser.parseSignedClaims(token).getPayload();
//     // }
// }

package com.app.Hospital.Management.System.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

private static final String SECRET = "0F1F2F3F4F5F6F7F8F9FAFBFCFDFEFF0F1F2F3F4F5F6F7F8F9FAFBFCFDFEFF"; // 64 chars = 256 bits
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;

    // private Key getSigningKey() {
    //     byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
    //     return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    // }

    public String generateToken(Long id,String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 10 hours
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256) // Correct usage
                .compact();
    }

    public String extractUsername(String token) {
        //System.out.println(token);
        try {
            Claims claims = extractAllClaims(token);
           
            return claims.getSubject();
        } catch (Exception e) {
            System.out.println("Error extracting username: " + e.getMessage()); // Debug: Log the error
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
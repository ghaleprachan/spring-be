package com.develop.api.util;

import com.develop.api.model.User;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtUtil {
    private static final String SECRET_KEY = "MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCDSGFjM8ngbFpkRLhqg2QHP5JiQxJP/8bTQ6qrSIhPhZA==";
    private static final String PUBLIC_KEY = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE48PKO/ygetzyse6OP/tGwrg0HARQ5x3hho6sbzqk5XN2TiVUz0QPJb5x+27kU9L5O/gqovjlH+JSO8rvH40c7w==";
    private final long EXPIRATION = 1000 * 60 * 60; // 1 hour

    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public String generateToken(User userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setAudience(userDetails.getAuthorities().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.ES256)
                .compact();
    }


    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }


    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }


    private Key getSigningKey() {
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(SECRET_KEY);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception ex) {
            return null;
        }
    }

    private Key getPublicKey() {
        try {
            byte[] publicKeyBytes = Base64.getDecoder().decode(PUBLIC_KEY);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            return keyFactory.generatePublic(publicKeySpec);
        } catch (Exception ex) {
            return null;
        }
    }
}
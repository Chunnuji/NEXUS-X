package com.nexus.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtUtil {

    private final String secretKey;

    public JwtUtil(String secretKey) {
        this.secretKey = secretKey;
    }

    public Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsername(Claims claims) {
        return claims.getSubject();
    }

    public String getRole(Claims claims) {
        return claims.get("role", String.class);
    }
}

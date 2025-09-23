package com.glympero.store.service;

import com.glympero.store.entities.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;

public class Jwt {
    private final Claims claims;
    private final SecretKey secretKey;

    public Jwt(Claims claims, SecretKey secretKey) {
        this.claims = claims;
        this.secretKey = secretKey;
    }

    public boolean isExpired() {
        try {
            return claims.getExpiration().before(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    public Long getUserId() {
        try {
            return Long.valueOf(claims.getSubject());
        } catch (JwtException e) {
            return null;
        }
    }

    public Role getRole() {
        try {
            return Role.valueOf((String) claims.get("role"));
        } catch (JwtException e) {
            return null;
        }
    }


    public String toString() {
        return Jwts.builder().claims(claims).signWith(secretKey).compact();
    }
}

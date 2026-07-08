package com.example.studentmanagement.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;


// when login succeeds admin-Jwtutil -creates:subject,issued,expiry- signs using secret key - jwt string
@Component
public class JwtUtil {

    private final String SECRET_KEY =
            "4d5a6460bd7d5134d1d55881c0e2094b25cad4aba6dffd6b0cbd11ba5d08d85b";

    private final long EXPIRATION_TIME =
            1000 * 60 * 60; // 1 hour


    public String generateToken(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis()
                                + EXPIRATION_TIME)
                )
                .signWith(
                        SignatureAlgorithm.HS256,
                        SECRET_KEY
                )
                .compact();
    }


    public String extractUsername(String token){

        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public boolean validateToken(String token){

        try {

            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);

            return true;

        }
        catch (JwtException e){

            return false;
        }
    }
}
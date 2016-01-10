package com.example.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenUtils {

    @Value("${demo.token.secret}")
    private String secret;
    @Value("${demo.token.expiration}")
    private long expiration;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            username = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    private Date getExpirationDate(String token) {
        Date expiration;
        try {
            expiration = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        final Date expiration = getExpirationDate(token);
        return username.equals(userDetails.getUsername()) && expiration.after(new Date(System.currentTimeMillis()));
    }

}

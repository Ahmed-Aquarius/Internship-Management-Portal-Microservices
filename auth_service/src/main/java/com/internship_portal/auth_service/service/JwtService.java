package com.internship_portal.auth_service.service;


import com.internship_portal.auth_service.model.*;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;


import java.security.Key;
import java.util.*;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecretKey;

    @Value("${jwt.expiration}")
    private long jwtExpirationPeriod;



    public String generateToken(String username, Set<String> roles) {

        List<String> rolesAsString = new ArrayList<>(roles);

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", rolesAsString)
                .setIssuedAt(new Date (System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationPeriod))
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
    }

}
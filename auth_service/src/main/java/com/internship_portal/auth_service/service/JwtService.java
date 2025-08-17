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



    public String generateToken(Long userId, String username, Set<Role> roles) {

        List<Role> rolesList = new ArrayList<>(roles);

        List<String> roleNamesStringList = rolesList.stream()
                .map(role -> role.getRole().toString())
                .toList();


        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roleNamesStringList)
                .setIssuedAt(new Date (System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationPeriod))
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
    }

}
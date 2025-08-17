package com.internship_portal.auth_service.util;


import com.internship_portal.auth_service.exception.TokenExpiredException;
import com.internship_portal.auth_service.exception.UsernameNotMatchUserIdException;
import com.internship_portal.auth_service.model.*;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;


import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

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

    public boolean isTokenValid(String token, User potentialUser) throws UsernameNotMatchUserIdException, TokenExpiredException {
        try {
            String username = extractUsername(token);

            if (!username.equals(potentialUser.getUsername())) {
                throw new UsernameNotMatchUserIdException();
            }

            if (isTokenExpired(token)) {
                throw new TokenExpiredException();
            }

            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            return true;

        } catch (JwtException | IllegalArgumentException e) {
            // JwtException covers signature issues, malformed tokens, expired tokens, etc.
            System.out.println("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }



    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Set<Role> extractRoles(User potentialUser, String token) {

        // Extract the list of role names (as strings) from the token
        List<String> rolesList = extractClaim(token, claims -> claims.get("roles", List.class));
        Set<String> roleStrings = new HashSet<>(rolesList);

        // Convert each string back to RoleName enum, then wrap into Role entity
        return roleStrings.stream()
                .map(roleStr -> {
                    Role role = null;
                    switch (roleStr) {
                        case "ADMIN":
                            role = new Admin(potentialUser, Role.RoleName.ADMIN);
                            break;
                        case "MENTOR":
                            role = new Mentor(potentialUser, Role.RoleName.MENTOR);
                            break;
                        case "INTERN":
                            role = new Intern(potentialUser, Role.RoleName.INTERN);
                            break;
                    }
                    return role;
                })
                .collect(Collectors.toSet());
    }



    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }



    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
    }

}
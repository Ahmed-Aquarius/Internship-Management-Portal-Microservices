package com.internship_portal.user_service.jwt_auth;



import com.internship_portal.user_service.exception.auth.AuthorizationHeaderNotFoundException;
import com.internship_portal.user_service.exception.auth.InvalidJwtTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil JwtUtil;



    @Autowired
    public JwtAuthFilter (JwtUtil JwtUtil) {
        this.JwtUtil = JwtUtil;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws IOException {

        try {
            
            // skipJWT validation for public endpoints
            String requestPath = request.getRequestURI();
            System.out.println("JWT Filter checking path: " + requestPath);
            if (isPublicEndpoint(requestPath)) {
                System.out.println("Skipping JWT validation for public endpoint: " + requestPath);
                filterChain.doFilter(request, response);
                return;
            }

            // if user is already authorized, don't re-authorize
            if (SecurityContextHolder.getContext().getAuthentication() != null)
            {
                filterChain.doFilter(request, response);
                return;
            }

            // extract auth header
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new AuthorizationHeaderNotFoundException();
            }

            // extract user details from the token extracted from header
            String token = authHeader.substring(7);     // Remove "Bearer " section
            String username = JwtUtil.extractUsername(token);
            Set<String> roles = JwtUtil.extractRoles(token);
            List<GrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role))
                    .collect(Collectors.toList());

            if (JwtUtil.isTokenValid(token))
            {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                throw new InvalidJwtTokenException();
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            System.out.println("JWT Filter Error: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }
    
    private boolean isPublicEndpoint(String requestPath) {
        return requestPath.startsWith("/api/users/username/") ||
               requestPath.equals("/api/users") ||
               requestPath.equals("/api/users/admins") ||
               requestPath.equals("/api/users/mentors") ||
               requestPath.equals("/api/users/interns") ||
               (requestPath.equals("/api/users") && "POST".equals("POST")); // For user registration
    }
}
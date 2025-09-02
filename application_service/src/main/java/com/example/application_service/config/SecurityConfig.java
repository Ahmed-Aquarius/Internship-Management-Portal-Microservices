package com.example.application_service.config;

import com.example.application_service.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                configurer
                        // Application endpoints - require authentication
                        .requestMatchers(HttpMethod.POST, "/api/v1/applications").hasAuthority("INTERN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/applications/by-intern/*").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/applications/by-internship/*").hasAnyAuthority("MENTOR", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/applications/*/status").hasAnyAuthority("MENTOR", "ADMIN")
                        // All other requests require authentication
                        .anyRequest().authenticated()
        );
        
        // Add JWT filter
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        // Session management
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
        // Disable CSRF for REST API
        http.csrf(csrf -> csrf.disable());
        
        return http.build();
    }
}

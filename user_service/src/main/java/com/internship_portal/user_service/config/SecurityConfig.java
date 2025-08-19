package com.internship_portal.user_service.config;

import com.internship_portal.user_service.jwt_auth.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;



    public SecurityConfig(final JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {

        JdbcUserDetailsManager UserDetailsManager = new JdbcUserDetailsManager(dataSource);

        UserDetailsManager.setUsersByUsernameQuery("select username, password, is_active from users where username=?");

        UserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT u.username, r.role " +
                        "FROM roles r " +
                        "JOIN users u ON r.user_id = u.id " +
                        "WHERE u.username=?"
        );

        return UserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer ->
                configurer
                        // Specific patterns first (more specific rules have precedence)
                        .requestMatchers(HttpMethod.GET, "/api/users/username/*").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/users",
                                "/api/users/admins",
                                "/api/users/mentors",
                                "/api/users/interns")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        // More general patterns last
                        .requestMatchers(HttpMethod.GET, "/api/users/*").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/users/*").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/*").hasAnyAuthority("MENTOR", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/*").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
        );

        // Add JWT filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.httpBasic(Customizer.withDefaults());

        http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}

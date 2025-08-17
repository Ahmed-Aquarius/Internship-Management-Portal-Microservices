package com.internship_portal.auth_service.config;

import com.internship_portal.auth_service.filter.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
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

    @Autowired
    public SecurityConfig(@Lazy JwtAuthFilter jwtAuthFilter) {
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
                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers(HttpMethod.GET,
                                "/api/users",
                                "/api/users/admins",
                                "/api/users/mentors",
                                "/api/users/interns")
                                .authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/users/*").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/users").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/users/*").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/*").hasAnyAuthority("MENTOR", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/*").hasAuthority("ADMIN")

                        //only admins can create/manage internships
                        .requestMatchers(HttpMethod.POST, "/api/v1/internships").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/internships/*").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/internships/*/status").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/internships/*").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/internships/**").authenticated()


                        //tasks
                        .requestMatchers(HttpMethod.GET,  "/api/v1/tasks/").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/tasks/").hasAnyAuthority("MENTOR","ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/api/v1/tasks/").hasAnyAuthority("MENTOR","ADMIN")
                        .requestMatchers(HttpMethod.PATCH,"/api/v1/tasks/").hasAnyAuthority("MENTOR","ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/tasks/").hasAnyAuthority("MENTOR","ADMIN")

                        // Submissions
                        .requestMatchers(HttpMethod.POST, "/api/v1/submissions/").hasAuthority("INTERN")
                        .requestMatchers(HttpMethod.GET,  "/api/v1/submissions/").authenticated()
                        .requestMatchers(HttpMethod.PATCH,"/api/v1/submissions/*/feedback").hasAnyAuthority("MENTOR","ADMIN")

                        // Applications
                        .requestMatchers(HttpMethod.POST, "/api/v1/applications/").hasAuthority("INTERN")
                        .requestMatchers(HttpMethod.GET,  "/api/v1/applications/").authenticated()
                        .requestMatchers(HttpMethod.PATCH,"/api/v1/applications/*/status").hasAnyAuthority("MENTOR","ADMIN")
                        .anyRequest().authenticated()
        );

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.httpBasic(Customizer.withDefaults());

        http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}

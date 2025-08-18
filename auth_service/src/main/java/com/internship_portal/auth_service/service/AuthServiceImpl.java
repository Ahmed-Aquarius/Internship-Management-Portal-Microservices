package com.internship_portal.auth_service.service;


import com.internship_portal.auth_service.dto.UserCredentialsDTO;
import com.internship_portal.auth_service.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService JwtService;

    public AuthServiceImpl(JwtService JwtService, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.JwtService = JwtService;
    }



    @Override
    public String authenticate(UserCredentialsDTO inputCredentials, User targetUser) {

        if (!passwordEncoder.matches(inputCredentials.password(), targetUser.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return JwtService.generateToken(targetUser.getUsername(), targetUser.getRoles());
    }
}

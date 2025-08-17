package com.internship_portal.auth_service.service;


import com.internship_portal.auth_service.dto.LoginDTO;
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
    public String authenticate(LoginDTO credentials) {

        //make a call to the user service to fetch the required details
        User candidateUser = userService.findUserByUsername(credentials.username());

        if (candidateUser == null) {
            throw new IllegalArgumentException("No user with such username");
        }

        if (!passwordEncoder.matches(credentials.password(), candidateUser.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return JwtService.generateToken(candidateUser.getId(), candidateUser.getUsername(), candidateUser.getRoles());
    }
}

package com.internship_portal.auth_service.service;


import com.internship_portal.auth_service.dto.LoginDTO;
import com.internship_portal.auth_service.model.User;
import com.internship_portal.auth_service.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }



    @Override
    public String authenticate(LoginDTO credentials) {

        User candidateUser = userService.findUserByUsername(credentials.username());

        if (candidateUser == null) {
            throw new IllegalArgumentException("No user with such username");
        }

        if (!passwordEncoder.matches(credentials.password(), candidateUser.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return jwtUtil.generateToken(candidateUser.getId(), candidateUser.getUsername(), candidateUser.getRoles());
    }
}

package com.internship_portal.auth_service.controller;

import com.internship_portal.auth_service.dto.LoginDTO;
import com.internship_portal.auth_service.model.User;
import com.internship_portal.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }



    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {

        //make a call to the userService to add the new user
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO credentials) {

        String jwtToken = authService.authenticate(credentials);
        return ResponseEntity.ok(jwtToken);
    }
}

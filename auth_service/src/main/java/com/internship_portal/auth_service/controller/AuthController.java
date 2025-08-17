package com.internship_portal.auth_service.controller;

import com.internship_portal.auth_service.dto.LoginDTO;
import com.internship_portal.auth_service.model.User;
import com.internship_portal.auth_service.service.AuthenticationService;
//import com.example.internship_portal.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public AuthController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }



    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {

        userService.addUser(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO credentials) {

        String jwtToken = authenticationService.authenticate(credentials);
        return ResponseEntity.ok(jwtToken);
    }
}

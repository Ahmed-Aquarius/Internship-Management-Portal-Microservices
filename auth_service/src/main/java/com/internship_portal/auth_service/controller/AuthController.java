package com.internship_portal.auth_service.controller;

import com.internship_portal.auth_service.dto.UserCredentialsDTO;
import com.internship_portal.auth_service.dto.RegisterUserDTO;
import com.internship_portal.auth_service.dto.UserDto;
import com.internship_portal.auth_service.model.User;
import com.internship_portal.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final RestTemplate restTemplate;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public AuthController(AuthService authService, RestTemplate restTemplate, WebClient.Builder webClientBuilder) {
        this.authService = authService;
        this.restTemplate = restTemplate;
        this.webClientBuilder = webClientBuilder;
    }



    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserDTO userDetails) {

//        RegisterUserDTO generatedUser = webClientBuilder.build()
//                .post()
//                .uri("http://user-service/api/users")
//                .bodyValue(userDetails)
//                .retrieve()
//                .bodyToMono(RegisterUserDTO.class)
//                .block();
//user-service

        //restTemplate.getForEntity("http://localhost:8087/api/users/admins", RegisterUserDTO.class);

        RegisterUserDTO generatedUser = restTemplate.postForEntity("http://localhost:8087/api/users", userDetails, RegisterUserDTO.class).getBody();

        String response = "User registered successfully!\n\n" + "the created user in the db is:\n" + generatedUser;

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserCredentialsDTO inputCredentials) {

        // Get the full user by username from User Service (temporary hard-coded for testing)
        UserDto.Response userResponse = webClientBuilder.build()
                .get()
                .uri("http://localhost:8087/api/users/username/" + inputCredentials.username())
                .retrieve()
                .bodyToMono(UserDto.Response.class)
                .block();

        if (userResponse == null) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        String jwtToken = authService.authenticate(inputCredentials, userResponse);

        return ResponseEntity.ok(jwtToken);
    }
}

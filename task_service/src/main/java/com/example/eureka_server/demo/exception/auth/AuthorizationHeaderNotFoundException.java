package com.example.eureka_server.demo.exception.auth;

public class AuthorizationHeaderNotFoundException extends Exception {
    public AuthorizationHeaderNotFoundException() {
        super("Authorization header missing or invalid");
    }
}

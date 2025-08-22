package com.example.eureka_server.demo2.exception.auth;

public class AuthorizationHeaderNotFoundException extends Exception {
    public AuthorizationHeaderNotFoundException() {
        super("Authorization header missing or invalid");
    }
}

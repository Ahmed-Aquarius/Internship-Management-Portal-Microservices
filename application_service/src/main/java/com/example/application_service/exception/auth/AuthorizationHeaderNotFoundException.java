package com.example.application_service.exception.auth;

public class AuthorizationHeaderNotFoundException extends RuntimeException {
    public AuthorizationHeaderNotFoundException(String message) {
        super(message);
    }
}

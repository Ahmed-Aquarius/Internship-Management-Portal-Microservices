package com.internship_portal.auth_service.exception.auth;

public class AuthorizationHeaderNotFoundException extends Exception {
    public AuthorizationHeaderNotFoundException() {
        super("Authorization header missing or invalid");
    }
}

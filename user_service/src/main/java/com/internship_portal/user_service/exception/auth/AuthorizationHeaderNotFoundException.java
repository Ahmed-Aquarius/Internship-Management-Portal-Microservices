package com.internship_portal.user_service.exception.auth;

public class AuthorizationHeaderNotFoundException extends Exception {
    public AuthorizationHeaderNotFoundException() {
        super("Authorization header missing or invalid");
    }
}

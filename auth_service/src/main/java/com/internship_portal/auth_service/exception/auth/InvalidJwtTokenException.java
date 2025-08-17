package com.internship_portal.auth_service.exception.auth;

public class InvalidJwtTokenException extends Exception {
    public InvalidJwtTokenException() {
        super("Invalid JWT token");
    }
}

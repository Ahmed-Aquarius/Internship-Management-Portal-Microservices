package com.internship_portal.auth_service.exception.auth;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super("Token is expired");
    }
}

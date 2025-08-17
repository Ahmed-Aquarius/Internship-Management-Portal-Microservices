package com.internship_portal.user_service.exception.auth;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super("Token is expired");
    }
}

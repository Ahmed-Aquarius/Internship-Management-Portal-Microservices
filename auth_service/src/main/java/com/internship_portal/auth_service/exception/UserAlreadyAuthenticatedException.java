package com.internship_portal.auth_service.exception;

public class UserAlreadyAuthenticatedException extends RuntimeException {
    public UserAlreadyAuthenticatedException() {
        super("user already authenticated");
    }
}

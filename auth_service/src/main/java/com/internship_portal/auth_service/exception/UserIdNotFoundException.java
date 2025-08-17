package com.internship_portal.auth_service.exception;

public class UserIdNotFoundException extends RuntimeException {
    public UserIdNotFoundException() {
        super("User not found");
    }
}

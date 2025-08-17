package com.internship_portal.auth_service.exception;

public class UsernameNotMatchUserIdException extends RuntimeException {
    public UsernameNotMatchUserIdException() {
        super("Username doesn't match user Id!");
    }
}

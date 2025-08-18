package com.example.internship_service.exception.auth;

public class UsernameNotMatchUserIdException extends RuntimeException {
    public UsernameNotMatchUserIdException() {
        super("Username doesn't match user Id!");
    }
}

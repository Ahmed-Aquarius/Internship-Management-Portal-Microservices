package com.example.eureka_server.demo.exception.auth;

public class UsernameNotMatchUserIdException extends RuntimeException {
    public UsernameNotMatchUserIdException() {
        super("Username doesn't match user Id!");
    }
}

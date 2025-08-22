package com.example.eureka_server.demo2.exception.auth;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super("Token is expired");
    }
}

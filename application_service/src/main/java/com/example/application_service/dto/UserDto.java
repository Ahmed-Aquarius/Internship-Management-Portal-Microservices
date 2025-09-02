package com.example.application_service.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class UserDto {
    
    public static record Response(
            Long id,
            String username,
            String email,
            String fullName,
            Boolean isActive,
            LocalDateTime createdAt,
            Set<RoleDto> roles
    ) { }
    
    public static record RoleDto(
            Long id,
            String role
    ) { }
}

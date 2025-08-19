package com.internship_portal.auth_service.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class UserDto {
    
    public static record Response(
            Long id,
            String username,
            String password,
            String email,
            String fullName,
            Boolean isActive,
            LocalDateTime createdAt,
            Set<RoleDto> roles
    ) {
        // Helper method to get simple role names for JWT generation
        public Set<String> getRoleNames() {
            return roles.stream()
                    .map(roleDto -> roleDto.role())
                    .collect(java.util.stream.Collectors.toSet());
        }
    }
    
    public static record RoleDto(
            Long id,
            String role
    ) { }
}

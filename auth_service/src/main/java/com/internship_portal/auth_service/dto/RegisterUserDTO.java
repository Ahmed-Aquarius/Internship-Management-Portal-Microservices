package com.internship_portal.auth_service.dto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public record RegisterUserDTO(
        Long id,
        String username,
        String password,
        String email,
        String fullName,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt,
        Set<Map<String, Object>> roles
) {}

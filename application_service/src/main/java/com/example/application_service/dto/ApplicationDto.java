package com.example.application_service.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class ApplicationDto {
    
    public static record CreateRequest(
            @NotNull(message = "Intern ID is required") 
            Long internId,
            
            @NotNull(message = "Internship ID is required") 
            Long internshipId,
            
            String coverLetter
    ) { }

    public static record UpdateRequest(
            @NotBlank(message = "Status is required")
            String status,
            
            Boolean isActive
    ) { }

    public static record Response(
            Long id,
            Long internId,
            Long internshipId,
            String status,
            String coverLetter,
            Boolean isActive,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) { }
}

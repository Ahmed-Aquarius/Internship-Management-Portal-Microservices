package com.example.internship_service.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class InternshipDto {
    
    public static record CreateRequest(
            @NotBlank String title,
            String description,
            String skills,
            @NotNull @Positive Integer duration,
            @NotNull LocalDate startDate,
            @NotNull LocalDate endDate,
            @NotNull @Positive Integer slots,
            @NotNull Long mentorId
    ) { }

    public static record UpdateRequest(
            String title,
            String description,
            String skills,
            @Positive Integer duration,
            LocalDate startDate,
            LocalDate endDate,
            @Positive Integer slots,
            String status,
            Boolean isActive
    ) { }

    public static record StatusUpdateRequest(
            @NotBlank String status
    ) { }

    public static record Response(
            Long id,
            String title,
            String description,
            String skills,
            Integer duration,
            LocalDate startDate,
            LocalDate endDate,
            Integer slots,
            String status,
            Boolean isActive,
            Long mentorId
    ) { }
}

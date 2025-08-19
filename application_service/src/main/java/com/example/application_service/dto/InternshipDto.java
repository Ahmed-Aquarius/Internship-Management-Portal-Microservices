package com.example.application_service.dto;

import java.time.LocalDate;

public class InternshipDto {
    
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

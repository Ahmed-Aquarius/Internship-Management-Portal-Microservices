package com.example.eureka_server.demo2.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class SubmissionDto {
    public record CreateRequest(@NotNull Long taskId, @NotNull Long internId, @NotBlank String content) {}
    public record FeedbackRequest(@NotBlank String feedback, @Min(0) @Max(100) Integer score, String status) {}
    public record Response(Long id, Long taskId, Long internId, String content, String status,
                           Integer score, String feedback, LocalDateTime submittedAt,
                           LocalDateTime reviewedAt, Boolean isActive){}

}

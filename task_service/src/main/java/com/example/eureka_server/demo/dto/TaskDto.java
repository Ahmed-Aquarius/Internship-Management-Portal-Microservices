package com.example.eureka_server.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class TaskDto {
    public record CreateRequest(
            @NotBlank String title,
            String description,
            @NotNull @FutureOrPresent LocalDate deadline,
            @NotNull Long internshipId) {}

    public record UpdateRequest(
            String title, String description,
            @FutureOrPresent LocalDate deadline,
            Boolean isActive) {}

    public record Response(
            Long id, String title, String description,
            LocalDate deadline, Boolean isActive, Long internshipId){}
}

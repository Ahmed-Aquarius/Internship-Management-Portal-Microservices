package com.example.application_service.controller;

import com.example.application_service.dto.ApplicationDto;
import com.example.application_service.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {
    
    private final ApplicationService applicationService;

    // Intern applies for an internship
    @PostMapping
    public ResponseEntity<ApplicationDto.Response> apply(@Valid @RequestBody ApplicationDto.CreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.apply(req));
    }

    // List an intern's applications
    @GetMapping("/by-intern/{internId}")
    public ResponseEntity<Page<ApplicationDto.Response>> listForIntern(
            @PathVariable Long internId, 
            Pageable pageable) {
        return ResponseEntity.ok(applicationService.listForIntern(internId, pageable));
    }

    // List applications for a specific internship (mentor/admin review queue)
    @GetMapping("/by-internship/{internshipId}")
    public ResponseEntity<Page<ApplicationDto.Response>> listForInternship(
            @PathVariable Long internshipId, 
            Pageable pageable) {
        return ResponseEntity.ok(applicationService.listForInternship(internshipId, pageable));
    }

    // Mentor/Admin accepts or rejects application
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationDto.Response> updateStatus(
            @PathVariable Long id, 
            @Valid @RequestBody ApplicationDto.UpdateRequest req) {
        return ResponseEntity.ok(applicationService.updateStatus(id, req));
    }
}

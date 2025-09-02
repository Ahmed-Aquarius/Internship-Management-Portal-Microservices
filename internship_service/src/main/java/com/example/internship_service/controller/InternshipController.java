package com.example.internship_service.controller;

import com.example.internship_service.dto.InternshipDto;
import com.example.internship_service.service.InternshipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/internships")
@RequiredArgsConstructor
public class InternshipController {
    
    private final InternshipService service;
    
    // Create internship
    @PostMapping
    public ResponseEntity<InternshipDto.Response> create(@Valid @RequestBody InternshipDto.CreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }
    
    // List internships with optional filters
    @GetMapping
    public ResponseEntity<List<InternshipDto.Response>> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String skills) {
        return ResponseEntity.ok(service.list(status, skills));
    }
    
    // Get internship by ID
    @GetMapping("/{id}")
    public ResponseEntity<InternshipDto.Response> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }
    
    // Update internship
    @PutMapping("/{id}")
    public ResponseEntity<InternshipDto.Response> update(
            @PathVariable Long id, 
            @Valid @RequestBody InternshipDto.UpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }
    
    // Update internship status
    @PatchMapping("/{id}/status")
    public ResponseEntity<InternshipDto.Response> updateStatus(
            @PathVariable Long id, 
            @Valid @RequestBody InternshipDto.StatusUpdateRequest req) {
        return ResponseEntity.ok(service.updateStatus(id, req.status()));
    }
    
    // Delete internship (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

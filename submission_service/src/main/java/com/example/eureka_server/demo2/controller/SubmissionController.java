package com.example.eureka_server.demo2.controller;

import com.example.eureka_server.demo2.dto.SubmissionDto;
import com.example.eureka_server.demo2.service.SubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/submissions")
@RequiredArgsConstructor
public class SubmissionController {
    private final SubmissionService service;

    @PostMapping
    public ResponseEntity<SubmissionDto.Response> create(@Valid @RequestBody SubmissionDto.CreateRequest req){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @GetMapping
    public Page<SubmissionDto.Response> list(@RequestParam Long taskId, Pageable pageable){
        return service.listByTask(taskId, pageable);
    }

    @PatchMapping("/{id}/feedback")
    public SubmissionDto.Response feedback(@PathVariable Long id, @Valid @RequestBody SubmissionDto.FeedbackRequest req){
        return service.giveFeedback(id, req);
}
}

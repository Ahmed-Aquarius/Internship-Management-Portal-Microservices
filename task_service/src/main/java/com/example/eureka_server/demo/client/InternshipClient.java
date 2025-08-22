package com.example.eureka_server.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="internship-service",path="/api/v1/internships")
public interface InternshipClient {
    @GetMapping("/{id}")
    InternshipSummary getById(@PathVariable("id") Long id);
}

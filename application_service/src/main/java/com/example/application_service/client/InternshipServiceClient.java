package com.example.application_service.client;

import com.example.application_service.dto.InternshipDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "internship-service", url = "http://localhost:8088", configuration = com.example.application_service.config.FeignConfig.class)
public interface InternshipServiceClient {
    
    @GetMapping("/api/v1/internships/{id}")
    InternshipDto.Response getInternship(@PathVariable("id") Long id);
}

package com.example.application_service.service;

import com.example.application_service.dto.ApplicationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationService {
    
    ApplicationDto.Response apply(ApplicationDto.CreateRequest req);
    
    Page<ApplicationDto.Response> listForIntern(Long internId, Pageable pageable);
    
    Page<ApplicationDto.Response> listForInternship(Long internshipId, Pageable pageable);
    
    ApplicationDto.Response updateStatus(Long id, ApplicationDto.UpdateRequest req);
}

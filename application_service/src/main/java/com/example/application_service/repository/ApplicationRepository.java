package com.example.application_service.repository;

import com.example.application_service.model.Application;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
    Page<Application> findByInternId(Long internId, Pageable pageable);
    
    Page<Application> findByInternshipId(Long internshipId, Pageable pageable);
    
    boolean existsByInternIdAndInternshipId(Long internId, Long internshipId);
    
    // Count applications by internship and status
    long countByInternshipIdAndStatus(Long internshipId, Application.ApplicationStatus status);
}

package com.example.application_service.service;

import com.example.application_service.dto.ApplicationDto;
import com.example.application_service.dto.UserDto;
import com.example.application_service.dto.InternshipDto;
import com.example.application_service.exception.BadRequestException;
import com.example.application_service.exception.ResourceNotFoundException;
import com.example.application_service.model.Application;
import com.example.application_service.repository.ApplicationRepository;
import com.example.application_service.client.UserServiceClient;
import com.example.application_service.client.InternshipServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationServiceImpl implements ApplicationService {
    
    private final ApplicationRepository appRepo;
    private final UserServiceClient userServiceClient;
    private final InternshipServiceClient internshipServiceClient;

    @Override
    public ApplicationDto.Response apply(ApplicationDto.CreateRequest req) {
        // Validate intern exists and has INTERN role
        UserDto.Response intern = userServiceClient.getUser(req.internId());
        if (intern == null) {
            throw new ResourceNotFoundException("Intern not found");
        }
        
        // Validate internship exists and is open
        InternshipDto.Response internship = internshipServiceClient.getInternship(req.internshipId());
        if (internship == null) {
            throw new ResourceNotFoundException("Internship not found");
        }
        
        if (!"ACTIVE".equals(internship.status()) || !internship.isActive()) {
            throw new BadRequestException("Internship is not open for applications");
        }
        
        // Check if intern already applied for this internship
        if (appRepo.existsByInternIdAndInternshipId(req.internId(), req.internshipId())) {
            throw new BadRequestException("You have already applied for this internship");
        }
        
        // Create application
        Application application = new Application();
        application.setInternId(req.internId());
        application.setInternshipId(req.internshipId());
        application.setCoverLetter(req.coverLetter());
        application.setStatus(Application.ApplicationStatus.PENDING);
        application.setIsActive(true);

        return toDto(appRepo.save(application));
    }

    @Override
    public Page<ApplicationDto.Response> listForIntern(Long internId, Pageable pageable) {
        return appRepo.findByInternId(internId, pageable).map(this::toDto);
    }

    @Override
    public Page<ApplicationDto.Response> listForInternship(Long internshipId, Pageable pageable) {
        return appRepo.findByInternshipId(internshipId, pageable).map(this::toDto);
    }

    @Override
    public ApplicationDto.Response updateStatus(Long id, ApplicationDto.UpdateRequest req) {
        Application application = appRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
                
        if (req.status() != null) {
            try {
                application.setStatus(Application.ApplicationStatus.valueOf(req.status().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid status. Valid values are: PENDING, ACCEPTED, REJECTED");
            }
        }
        
        if (req.isActive() != null) {
            application.setIsActive(req.isActive());
        }
        
        return toDto(appRepo.save(application));
    }

    private ApplicationDto.Response toDto(Application application) {
        return new ApplicationDto.Response(
                application.getId(),
                application.getInternId(),
                application.getInternshipId(),
                application.getStatus().name(),
                application.getCoverLetter(),
                application.getIsActive(),
                application.getCreatedAt(),
                application.getUpdatedAt()
        );
    }
}

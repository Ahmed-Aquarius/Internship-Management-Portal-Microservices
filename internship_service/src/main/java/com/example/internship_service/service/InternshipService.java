package com.example.internship_service.service;

import com.example.internship_service.client.UserServiceClient;
import com.example.internship_service.dto.InternshipDto;
import com.example.internship_service.dto.UserDto;
import com.example.internship_service.model.Internship;
import com.example.internship_service.repository.InternshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InternshipService {
    
    private final InternshipRepository internshipRepo;
    private final UserServiceClient userServiceClient;
    
    public InternshipDto.Response create(InternshipDto.CreateRequest req) {
        // Validate mentor exists and has MENTOR role via Feign client
        UserDto.Response mentor;
        try {
            mentor = userServiceClient.getUser(req.mentorId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentor not found");
        }
        
        // Check if user has MENTOR role
        Set<String> userRoles;
        try {
            userRoles = userServiceClient.getUserRoles(req.mentorId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch user roles");
        }
        
        if (!userRoles.contains("MENTOR")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User must have MENTOR role to be assigned to internship");
        }
        
        // Validate dates
        if (req.startDate().isAfter(req.endDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date must be before end date");
        }
        
        Internship internship = new Internship();
        internship.setTitle(req.title());
        internship.setDescription(req.description());
        internship.setSkills(req.skills());
        internship.setDuration(req.duration());
        internship.setStartDate(req.startDate());
        internship.setEndDate(req.endDate());
        internship.setSlots(req.slots());
        internship.setMentorId(req.mentorId());
        internship.setStatus(Internship.InternshipStatus.OPEN);
        internship.setIsActive(true);
        
        return toDto(internshipRepo.save(internship));
    }
    
    // List internships with optional filters
    public List<InternshipDto.Response> list(String status, String skills) {
        List<Internship> internships;
        
        if (status != null && skills != null) {
            // Both filters
            try {
                Internship.InternshipStatus statusEnum = Internship.InternshipStatus.valueOf(status.toUpperCase());
                internships = internshipRepo.findByStatusAndSkillsContainingIgnoreCase(statusEnum, skills);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status: " + status);
            }
        } else if (status != null) {
            // Only status filter
            try {
                Internship.InternshipStatus statusEnum = Internship.InternshipStatus.valueOf(status.toUpperCase());
                internships = internshipRepo.findByStatusAndIsActiveTrue(statusEnum);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status: " + status);
            }
        } else if (skills != null) {
            // Only skills filter
            internships = internshipRepo.findBySkillsContainingIgnoreCase(skills);
        } else {
            // No filters - return all active
            internships = internshipRepo.findByIsActiveTrue();
        }
        
        return internships.stream().map(this::toDto).toList();
    }
    
    public InternshipDto.Response get(Long id) {
        Internship internship = internshipRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Internship not found"));
        
        if (!internship.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Internship not found");
        }
        
        return toDto(internship);
    }
    
    public InternshipDto.Response update(Long id, InternshipDto.UpdateRequest req) {
        Internship internship = internshipRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Internship not found"));
        
        if (!internship.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Internship not found");
        }
        
        // Update fields if provided
        if (req.title() != null) internship.setTitle(req.title());
        if (req.description() != null) internship.setDescription(req.description());
        if (req.skills() != null) internship.setSkills(req.skills());
        if (req.duration() != null) internship.setDuration(req.duration());
        if (req.startDate() != null) internship.setStartDate(req.startDate());
        if (req.endDate() != null) internship.setEndDate(req.endDate());
        if (req.slots() != null) internship.setSlots(req.slots());
        if (req.isActive() != null) internship.setIsActive(req.isActive());
        
        // Validate dates if both are provided
        if (req.startDate() != null && req.endDate() != null && req.startDate().isAfter(req.endDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date must be before end date");
        }
        
        return toDto(internshipRepo.save(internship));
    }
    
    public InternshipDto.Response updateStatus(Long id, String status) {
        Internship internship = internshipRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Internship not found"));
        
        if (!internship.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Internship not found");
        }
        
        try {
            Internship.InternshipStatus statusEnum = Internship.InternshipStatus.valueOf(status.toUpperCase());
            internship.setStatus(statusEnum);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status: " + status);
        }
        
        return toDto(internshipRepo.save(internship));
    }
    
    public void delete(Long id) {
        Internship internship = internshipRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Internship not found"));
        
        internship.setIsActive(false);
        internshipRepo.save(internship);
    }
    
    private InternshipDto.Response toDto(Internship internship) {
        return new InternshipDto.Response(
                internship.getId(),
                internship.getTitle(),
                internship.getDescription(),
                internship.getSkills(),
                internship.getDuration(),
                internship.getStartDate(),
                internship.getEndDate(),
                internship.getSlots(),
                internship.getStatus().name(),
                internship.getIsActive(),
                internship.getMentorId()
        );
    }
}

package com.example.internship_service.repository;

import com.example.internship_service.model.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InternshipRepository extends JpaRepository<Internship, Long> {
    
    // Find by status
    List<Internship> findByStatusAndIsActiveTrue(Internship.InternshipStatus status);
    
    // Find by skills (case-insensitive contains)
    @Query("SELECT i FROM Internship i WHERE i.isActive = true AND LOWER(i.skills) LIKE LOWER(CONCAT('%', :skills, '%'))")
    List<Internship> findBySkillsContainingIgnoreCase(@Param("skills") String skills);
    
    // Find by status and skills
    @Query("SELECT i FROM Internship i WHERE i.isActive = true AND i.status = :status AND LOWER(i.skills) LIKE LOWER(CONCAT('%', :skills, '%'))")
    List<Internship> findByStatusAndSkillsContainingIgnoreCase(@Param("status") Internship.InternshipStatus status, @Param("skills") String skills);
    
    // Find all active internships
    List<Internship> findByIsActiveTrue();
}

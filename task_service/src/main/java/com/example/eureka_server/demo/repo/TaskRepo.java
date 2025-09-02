package com.example.eureka_server.demo.repo;

import com.example.eureka_server.demo.model.Task;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepo  extends JpaRepository<Task, Long> {
    Page<Task> findByIsActiveTrue(Pageable pageable);
    Page<Task> findByInternshipId(Long internshipId, Pageable pageable);

    boolean existsById(Long id);

}

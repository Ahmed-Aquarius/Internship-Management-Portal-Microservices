package com.example.eureka_server.demo2.repo;

import com.example.eureka_server.demo2.model.Submission;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepo extends JpaRepository<Submission,Long> {
    Page<Submission> findByTaskId(Long taskId, Pageable pageable);

}

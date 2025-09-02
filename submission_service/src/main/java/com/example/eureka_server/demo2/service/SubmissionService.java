package com.example.eureka_server.demo2.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.eureka_server.demo2.dto.SubmissionDto;
public interface SubmissionService {
    SubmissionDto.Response create(SubmissionDto.CreateRequest req);
    Page<SubmissionDto.Response> listByTask(Long taskId, Pageable pageable);
    SubmissionDto.Response giveFeedback(Long id, SubmissionDto.FeedbackRequest req);
}

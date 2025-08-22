package com.example.eureka_server.demo2.service;

import com.example.eureka_server.demo2.client.TaskClient;
import com.example.eureka_server.demo2.dto.SubmissionDto;
import com.example.eureka_server.demo2.exception.BadRequestException;
import com.example.eureka_server.demo2.exception.ResourceNotFoundException;
import com.example.eureka_server.demo2.model.Submission;
import com.example.eureka_server.demo2.repo.SubmissionRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {
    private final SubmissionRepo repo;
    private final TaskClient taskClient;

    private SubmissionDto.Response toDto(Submission s){
        return new SubmissionDto.Response(
                s.getId(), s.getTaskId(), s.getInternId(), s.getContent(),
                s.getStatus().name(), s.getScore(), s.getFeedback(),
                s.getSubmittedAt(), s.getReviewedAt(), s.getIsActive());
    }

    @Transactional
    public SubmissionDto.Response create(SubmissionDto.CreateRequest req){
        // validate task via task-service
        boolean exists = Boolean.TRUE.equals(taskClient.exists(req.taskId()).get("exists"));
        if (!exists) throw new ResourceNotFoundException("Task not found");

        Submission s = new Submission();
        s.setTaskId(req.taskId());
        s.setInternId(req.internId());
        s.setContent(req.content());
        s.setStatus(Submission.SubmissionStatus.SUBMITTED);
        s.setIsActive(true);
        return toDto(repo.save(s));
    }

    public Page<SubmissionDto.Response> listByTask(Long taskId, Pageable pageable){
        return repo.findByTaskId(taskId, pageable).map(this::toDto);
    }

    @Transactional
    public SubmissionDto.Response giveFeedback(Long id, SubmissionDto.FeedbackRequest req){
        Submission s = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Submission not found"));
        s.setFeedback(req.feedback());
        s.setScore(req.score());
        if (req.status()!=null) {
            try { s.setStatus(Submission.SubmissionStatus.valueOf(req.status())); }
            catch (IllegalArgumentException e) { throw new BadRequestException("Invalid status"); }
        }
        s.setReviewedAt(LocalDateTime.now());
        return toDto(repo.save(s));
}
}

package com.example.eureka_server.demo.services;

import com.example.eureka_server.demo.client.InternshipClient;
import com.example.eureka_server.demo.dto.TaskDto;
import com.example.eureka_server.demo.exception.ResourceNotFoundException;
import com.example.eureka_server.demo.model.Task;
import com.example.eureka_server.demo.repo.TaskRepo;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class TaskServicesImpl implements TaskService {
    private final TaskRepo repo;
    private final InternshipClient internshipClient;

    private TaskDto.Response toDto(Task t) {
        return new TaskDto.Response(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.getDeadline(),
                t.getIsActive(),
                t.getInternshipId()
        );
    }

    /**
     * Validate internshipId by calling internship-service.
     * If internship-service returns 404, we translate to our 404.
     */
    private void assertInternshipExists(Long internshipId) {
        try {
            var _ = internshipClient.getById(internshipId); // body not used; 200 => exists
        } catch (FeignException.NotFound nf) {
            throw new ResourceNotFoundException("Internship not found: " + internshipId);
        } catch (FeignException fx) {
            // other HTTP errors from internship-service
            throw new RuntimeException("Internship service error: " + fx.status(), fx);
        }
    }

    @Override
    @Transactional
    public TaskDto.Response create(TaskDto.CreateRequest req) {
        assertInternshipExists(req.internshipId());

        Task t = new Task();
        t.setTitle(req.title());
        t.setDescription(req.description());
        t.setDeadline(req.deadline());
        t.setInternshipId(req.internshipId());
        t.setIsActive(true);

        return toDto(repo.save(t));
    }
    @Override
    @Transactional
    public Page<TaskDto.Response> list(Long internshipId, Pageable pageable) {
        Page<Task> page = (internshipId == null)
                ? repo.findByIsActiveTrue(pageable)
                : repo.findByInternshipId(internshipId, pageable);
        return page.map(this::toDto);
    }

    @Override
    @Transactional
    public TaskDto.Response get(Long id) {
        Task t = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
        return toDto(t);
    }

    @Override
    @Transactional
    public TaskDto.Response update(Long id, TaskDto.UpdateRequest req) {
        Task t = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));

        if (req.title() != null) t.setTitle(req.title());
        if (req.description() != null) t.setDescription(req.description());
        if (req.deadline() != null) t.setDeadline(req.deadline());
        if (req.isActive() != null) t.setIsActive(req.isActive());

        return toDto(repo.save(t));
    }
    @Override
    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Task not found: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repo.existsById(id);
    }


}

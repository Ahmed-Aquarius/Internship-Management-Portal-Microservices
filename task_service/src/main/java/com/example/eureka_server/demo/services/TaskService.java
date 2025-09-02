package com.example.eureka_server.demo.services;

import com.example.eureka_server.demo.dto.TaskDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskDto.Response create(TaskDto.CreateRequest req);
    Page<TaskDto.Response> list(Long internshipId, Pageable pageable);
    TaskDto.Response get(Long id);
    TaskDto.Response update(Long id, TaskDto.UpdateRequest req);
    void delete(Long id);
    boolean existsById(Long id);
}

package com.example.eureka_server.demo.Controller;

import com.example.eureka_server.demo.dto.TaskDto;
import com.example.eureka_server.demo.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService service;
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDto.Response> create(@Valid @RequestBody TaskDto.CreateRequest req){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @GetMapping
    public Page<TaskDto.Response> list(@RequestParam(required=false) Long internshipId, Pageable pageable){
        return service.list(internshipId, pageable);
    }

    @GetMapping("/{id}")
    public TaskDto.Response get(@PathVariable Long id){ return service.get(id); }

    @PatchMapping("/{id}")
    public TaskDto.Response update(@PathVariable Long id, @Valid @RequestBody TaskDto.UpdateRequest req){
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){ service.delete(id); }


   @GetMapping("/internal/{id}/exists")
    public ResponseEntity<Map<String ,Boolean>> exists(@PathVariable("id") Long id){
        boolean exists =taskService.existsById(id);
        return ResponseEntity.ok(Map.of("exists", exists));
   }
}

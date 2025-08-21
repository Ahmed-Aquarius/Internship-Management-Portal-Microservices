package com.example.eureka_server.demo2.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "task-service", path = "/api/v1/tasks")
public interface TaskClient {
    @GetMapping("/internal/{id}/exists")
    Map<String, Boolean> exists(@PathVariable("id") Long id);
}
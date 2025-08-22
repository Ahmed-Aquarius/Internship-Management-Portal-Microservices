package com.example.application_service.client;

import com.example.application_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@FeignClient(name = "user-service", url = "http://localhost:8087", configuration = com.example.application_service.config.FeignConfig.class)
public interface UserServiceClient {
    
    @GetMapping("/api/users/{id}")
    UserDto.Response getUser(@PathVariable("id") Long id);
    
    @GetMapping("/api/users/{id}/roles")
    Set<String> getUserRoles(@PathVariable("id") Long id);
}

package com.internship_portal.auth_service.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@Data
public class User {

    private Long id;
    
   protected String username;
    
   protected String password;
    
    protected String email;
    
    protected Boolean isActive = true;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime lastUpdatedAt = LocalDateTime.now();

    protected Set<Role> roles = new HashSet<>();



    public enum Role {
        INTERN, MENTOR, ADMIN
    }



    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }



    public Boolean getActive() {
        return isActive;
    }


    public LocalDateTime getUpdatedAt() {
        return lastUpdatedAt;
    }





    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.lastUpdatedAt = updatedAt;
    }

    public void setRoles(Set<Role> roles) {
        this.roles.clear();
        this.roles = roles;
    }




    public void addRoles(Role... roles) {
        this.roles.addAll(Arrays.asList(roles));
    }

    public void removeRoles(Role... roles) {
        Arrays.stream(roles).toList().forEach(this.roles::remove);
    }
}

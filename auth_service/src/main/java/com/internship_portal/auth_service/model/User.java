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


@Entity
@Table(name = "users")
@Data
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username", unique = true, nullable = false, length = 50)
    @NotBlank(message = "Username is required")
    @Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters")
    protected String username;
    
    @Column(name = "password", nullable = false, length = 68)
    @NotBlank(message = "Password is required")
    protected String password;
    
    @Column(name = "email", unique = true, nullable = false, length = 100)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    protected String email;
    
    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    protected Boolean isActive = true;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @LastModifiedDate
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime lastUpdatedAt = LocalDateTime.now();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user-role",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
    @NotEmpty(message = "User must have at least one role")
    @Valid
    protected Set<Role> roles = new HashSet<>();



    public enum Role {
        INTERN, MENTOR, ADMIN
    }



    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String username, String password, String email, Set<Role> roles) {
        this (username, password, email);
        this.roles = roles;
    }

    public User () {}




    public @NotBlank(message = "Username is required") @Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters") String getUsername() {
        return username;
    }

    public @NotBlank(message = "Password is required") String getPassword() {
        return password;
    }

    public Boolean getActive() {
        return isActive;
    }

    public @Email(message = "Email should be valid") @NotBlank(message = "Email is required") String getEmail() {
        return email;
    }

    public LocalDateTime getUpdatedAt() {
        return lastUpdatedAt;
    }




    public void setUsername(@NotBlank(message = "Username is required") @Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters") String username) {
        this.username = username;
    }

    public void setPassword(@NotBlank(message = "Password is required") String password) {
        this.password = password;
    }

    public void setEmail(@Email(message = "Email should be valid") @NotBlank(message = "Email is required") String email) {
        this.email = email;
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

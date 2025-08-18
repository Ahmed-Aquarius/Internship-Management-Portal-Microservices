package com.internship_portal.user_service.model;

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

    @Column(name = "full_name", nullable = false, length = 100)
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name cannot exceed 100 characters")
    protected String fullName;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    protected Boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime lastUpdatedAt = LocalDateTime.now();

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @NotEmpty(message = "User must have at least one role")
    @Valid
    protected Set<Role> roles = new HashSet<>();




    public User(String username, String password, String email, String fullName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
    }

    public User(String username, String password, String email, String fullName, Set<Role> roles) {
        this (username, password, email, fullName);
        this.roles = roles;
    }

    public User () {}




    public @NotBlank(message = "Username is required") @Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters") String getUsername() {
        return username;
    }

    public @NotBlank(message = "Password is required") String getPassword() {
        return password;
    }

    public @NotBlank(message = "Full name is required") @Size(max = 100, message = "Full name cannot exceed 100 characters") String getFullName() {
        return fullName;
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

    public void setFullName(@NotBlank(message = "Full name is required") @Size(max = 100, message = "Full name cannot exceed 100 characters") String fullName) {
        this.fullName = fullName;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.lastUpdatedAt = updatedAt;
    }

    public void setRoles(Set<Role> roles) {
        this.roles.clear();

        if (roles != null) {
            for (Role role : roles) {
                addRoles(role);
            }
        }
    }




    public void addRoles(Role... roles) {
        for (Role role : roles) {
            role.setUser(this);
            this.roles.add(role);
        }
    }

    public void removeRoles(Role... roles) {
        Arrays.stream(roles).toList().forEach(this.roles::remove);
    }
}

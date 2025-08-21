package com.example.eureka_server.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="tasks")
@Getter
@Setter

public class Task {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable=false)
    private LocalDate deadline;

    @Column(nullable=false)
    private Boolean isActive = true;

    // point to Internship service by id only
    @Column(nullable=false)
    private Long internshipId;

}

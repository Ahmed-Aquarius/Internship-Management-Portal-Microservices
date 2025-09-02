package com.example.eureka_server.demo2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="submissions")
@Getter
@Setter
public class Submission {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false) private Long taskId;
    @Column(nullable=false) private Long internId;

    @Column(columnDefinition="TEXT") private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false) private SubmissionStatus status = SubmissionStatus.SUBMITTED;

    private Integer score;
    private String feedback;

    @Column(nullable=false) private Boolean isActive = true;

    @Column(nullable=false) private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;

    @PrePersist void onCreate(){ submittedAt = LocalDateTime.now(); }

    public enum SubmissionStatus { SUBMITTED, REVIEWED, APPROVED, REJECTED}
}

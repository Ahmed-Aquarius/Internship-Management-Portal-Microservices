package com.internship_portal.user_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "interns")
@Data
@PrimaryKeyJoinColumn(name = "user-role_id")
public class Intern extends Role {

    @Column(name = "school", length= 50)
    private String school;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "intern_skills",
            joinColumns = @JoinColumn(name = "user-role_id")
    )
    @Column(name = "skill")
    private Set<String> skills = new HashSet<>();


    public Intern(User user, Role.RoleName role, String school, Set<String> skills) {
        super(user, role);
        this.school = school;
        this.skills = skills;
    }

    public Intern(User user, Role.RoleName role) {
        this(user, role, null, null);
    }

    public Intern () {
        super(Role.RoleName.INTERN);
    }

}

package com.internship_portal.user_service.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "mentors")
@Data
@PrimaryKeyJoinColumn(name = "user-role_id")
public class Mentor extends Role {

    @Column(name = "company", length = 60)
    private String company;

    @Column(name = "position", length = 80)
    private String position;



    public Mentor(User user, Role.RoleName role, String company, String position) {
        super(user, role);
        this.company = company;
        this.position = position;
    }

    public Mentor(User user, Role.RoleName role) {
        this (user, role, null, null);
    }

    public Mentor() {
        super(Role.RoleName.MENTOR);
    }



    public String getCompany() {
        return company;
    }

    public String getPosition() {
        return position;
    }


    public void setCompany(String companyName) {
        this.company = companyName;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}

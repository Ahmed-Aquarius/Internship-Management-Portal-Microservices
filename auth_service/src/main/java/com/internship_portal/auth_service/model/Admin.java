package com.internship_portal.auth_service.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "admins")
@Data
@PrimaryKeyJoinColumn(name = "user-role_id")
public class Admin extends Role {

    @Column(name = "company", length = 60)
    private String company;


    public Admin(User user, Role.RoleName role, String company) {
        super(user, role);
        this.company = company;
    }

    public Admin(User user, Role.RoleName role) {
        this(user, role, null);
    }

    public Admin() {
        super(Role.RoleName.ADMIN);
    }



    public String getCompany() {
        return company;
    }


    public void setCompany(String companyName) {
        this.company = companyName;
    }
}

package com.internship_portal.user_service.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Data;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "role",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Intern.class, name = "INTERN"),
        @JsonSubTypes.Type(value = Mentor.class, name = "MENTOR"),
        @JsonSubTypes.Type(value = Admin.class, name = "ADMIN")
})

@Entity
@Table(name = "roles")
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleName role;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;



    public Role(User user, RoleName role) {
        this.user = user;
        this.role = role;
    }

    public Role(RoleName role) {
        this.role = role;
    }

    public Role() {}



    public enum RoleName {
        INTERN, MENTOR, ADMIN
    }

}


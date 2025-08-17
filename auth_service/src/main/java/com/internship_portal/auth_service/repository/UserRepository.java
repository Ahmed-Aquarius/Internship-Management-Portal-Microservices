package com.internship_portal.auth_service.repository;


import com.internship_portal.auth_service.model.Role;
import com.internship_portal.auth_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r.role = :roleName")
    List<User> findUsersByRole(@Param("roleName") Role.RoleName roleName);

    Optional<User> findByUsername(String username);

}

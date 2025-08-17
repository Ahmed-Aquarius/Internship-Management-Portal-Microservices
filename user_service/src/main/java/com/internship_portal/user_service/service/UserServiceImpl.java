package com.internship_portal.user_service.service;

import com.internship_portal.user_service.model.*;
import com.internship_portal.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    public List<User> findAllUsers () {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAllAdmins () {
        return userRepository.findUsersByRole(Role.RoleName.ADMIN);
    }

    @Override
    public List<User> findAllMentors () {
        return userRepository.findUsersByRole(Role.RoleName.MENTOR);
    }

    @Override
    public List<User> findAllInterns () {
        return userRepository.findUsersByRole(Role.RoleName.INTERN);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findUserByUsername(String username) {return userRepository.findByUsername(username).orElse(null);}

    @Override
    public User addUser(User newUser) {

        //add password validation and other validations

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        return userRepository.save(newUser);
    }

    @Override
    public User updateUser(Long userId, User patchPayLoad) {

        User targetUser = findUserById(userId);

        if (patchPayLoad.getUsername() != null) {targetUser.setUsername(patchPayLoad.getUsername());}
        if (patchPayLoad.getPassword() != null) {targetUser.setPassword(patchPayLoad.getPassword());}
        if (patchPayLoad.getEmail() != null) {targetUser.setEmail(patchPayLoad.getEmail());}
        if (patchPayLoad.getFullName() != null) {targetUser.setFullName(patchPayLoad.getFullName());}
        if (patchPayLoad.getActive() != null) {targetUser.setActive(patchPayLoad.getActive());}

        if (patchPayLoad.getRoles() != null) {
            Set<Role> roles = patchPayLoad.getRoles();

            for (Role role : roles) {
                if (role instanceof Admin admin)
                {
                    Role targetUserRole = targetUser.getRoles().stream()
                            .filter(tempRole -> "ADMIN".equals(tempRole.getRole().toString()))
                            .findFirst()
                            .orElse(null);

                    boolean isNewRole = targetUserRole == null;

                    if (isNewRole)
                    {
                        targetUserRole = new Admin();
                    }

                    if (admin.getCompany() != null)
                    {
                        ((Admin) targetUserRole).setCompany(admin.getCompany());
                    }

                    if (isNewRole)
                    {
                        targetUser.addRoles(targetUserRole);
                    }
                }

                if (role instanceof Mentor mentor)
                {
                    Role targetUserRole = targetUser.getRoles().stream()
                            .filter(tempRole -> "MENTOR".equals(tempRole.getRole().toString()))
                            .findFirst()
                            .orElse(null);

                    boolean isNewRole = targetUserRole == null;

                    if (isNewRole)
                    {
                        targetUserRole = new Mentor();
                    }

                    if (mentor.getCompany() != null)
                    {
                        ((Mentor) targetUserRole).setCompany(mentor.getCompany());
                    }
                    if (mentor.getPosition() != null)
                    {
                        ((Mentor) targetUserRole).setPosition(mentor.getPosition());
                    }

                    if (isNewRole)
                    {
                        targetUser.addRoles(targetUserRole);
                    }
                }

                if (role instanceof Intern intern)
                {
                    Role targetUserRole = targetUser.getRoles().stream()
                            .filter(tempRole -> "INTERN".equals(tempRole.getRole().toString()))
                            .findFirst()
                            .orElse(null);

                    boolean isNewRole = targetUserRole == null;

                    if (isNewRole)
                    {
                        targetUserRole = new Intern();
                    }

                    if (intern.getSchool() != null)
                    {
                        ((Intern) targetUserRole).setSchool(intern.getSchool());
                    }
                    if (intern.getSkills() != null)
                    {
                        ((Intern) targetUserRole).setSkills(intern.getSkills());
                    }

                    if (isNewRole)
                    {
                        targetUser.addRoles(targetUserRole);
                    }
                }
            }
        }

        targetUser.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(targetUser);
    }

    @Override
    public User replaceUser(Long userId, User userNewData) {

        User targetUser = findUserById(userId);

        targetUser.setUsername(userNewData.getUsername());
        targetUser.setPassword(userNewData.getPassword());
        targetUser.setEmail(userNewData.getEmail());
        targetUser.setFullName(userNewData.getFullName());
        targetUser.setActive(userNewData.getActive());

        //targetUser.setRoles(userNewData.getRoles());

        Map<Long, Role> existingRolesById = targetUser.getRoles().stream()
                .filter(r -> r.getId() != null)
                .collect(Collectors.toMap(Role::getId, r -> r));

        Set<Role> updatedRoles = new HashSet<>();

        for (Role newRole : userNewData.getRoles()) {
            if (newRole.getId() != null && existingRolesById.containsKey(newRole.getId())) {
                Role existingRole = existingRolesById.get(newRole.getId());
                existingRole.setRole(newRole.getRole());

                if (existingRole instanceof Mentor && newRole instanceof Mentor) {
                    ((Mentor) existingRole).setPosition(((Mentor) newRole).getPosition());
                    ((Mentor) existingRole).setCompany(((Mentor) newRole).getCompany());
                } else if (existingRole instanceof Intern && newRole instanceof Intern) {
                    ((Intern) existingRole).setSchool(((Intern) newRole).getSchool());
                    ((Intern) existingRole).setSkills(((Intern) newRole).getSkills());
                } else if (existingRole instanceof Admin && newRole instanceof Admin) {
                    ((Admin) existingRole).setCompany(((Admin) newRole).getCompany());
                }

                updatedRoles.add(existingRole);
            } else {
                newRole.setUser(targetUser);
                updatedRoles.add(newRole);
            }
        }

        targetUser.getRoles().retainAll(updatedRoles);

        targetUser.getRoles().addAll(updatedRoles);

        targetUser.setUpdatedAt(LocalDateTime.now());

        return targetUser;
    }


    @Override
    public void deleteUser (Long userID) {
        userRepository.deleteById(userID);
    }

}

package com.internship_portal.user_service.service;

import com.internship_portal.user_service.model.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    List<User> findAllUsers();

    List<User> findAllAdmins();

    List<User> findAllMentors();

    List<User> findAllInterns();


    User findUserById(Long id);

    User findUserByUsername(String username);


    @Transactional
    User addUser(User newUser);

    @Transactional
    User updateUser(Long userId, User patchPayLoad);

    @Transactional
    User replaceUser(Long userId, User userNewData);

    @Transactional
    void deleteUser(Long userID);

}

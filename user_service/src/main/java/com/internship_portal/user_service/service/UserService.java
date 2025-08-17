package com.internship_portal.user_service.service;

import com.internship_portal.user_service.model.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    public List<User> findAllUsers ();

    public List<User> findAllAdmins ();

    public List<User> findAllMentors ();

    public List<User> findAllInterns ();


    public User findUserById(Long id);

    public User findUserByUsername(String username);


    @Transactional
    public User addUser(User newUser);

    @Transactional
    public User updateUser(Long userId, User patchPayLoad);

    @Transactional
    public User replaceUser(Long userId, User userNewData);

    @Transactional
    public void deleteUser (Long userID);

}

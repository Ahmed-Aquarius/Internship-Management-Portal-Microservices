package com.internship_portal.user_service.controller;

import com.internship_portal.user_service.model.User;
import com.internship_portal.user_service.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImpl userServiceImpl;


    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }



    @GetMapping()
    public List<User> getAllUsers() {
        return userServiceImpl.findAllUsers();
    }

    @GetMapping("/admins")
    public List<User> getAllAdmins() {
        return userServiceImpl.findAllAdmins();
    }

    @GetMapping("/mentors")
    public List<User> getAllMentors() {
        return userServiceImpl.findAllMentors();
    }

    @GetMapping("/interns")
    public List<User> getAllInterns() {
        return userServiceImpl.findAllInterns();
    }

    @GetMapping("{id}")
    public User getUserById(@PathVariable Long id) {
        return userServiceImpl.findUserById(id);
    }

    @PostMapping()
    public User createUser(@RequestBody User newUser) {
        return userServiceImpl.addUser(newUser);
    }

    @PutMapping("{id}")
    public User updateUserTotally(@PathVariable Long id, @RequestBody User updatedUser) {
        return userServiceImpl.replaceUser(id, updatedUser);
    }

    @PatchMapping("{id}")
    public User updateUserPartially(@PathVariable Long id, @RequestBody User patchPayLoad) {
        return userServiceImpl.updateUser(id, patchPayLoad);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable Long id) {
        userServiceImpl.deleteUser(id);
    }

}

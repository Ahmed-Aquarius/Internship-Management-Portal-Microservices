package com.internship_portal.user_service.controller;

import com.internship_portal.user_service.dto.UserCredentialsDTO;
import com.internship_portal.user_service.exception.NoSuchUserException;
import com.internship_portal.user_service.model.User;
import com.internship_portal.user_service.repository.UserRepository;
import com.internship_portal.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }



    @GetMapping()
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/admins")
    public List<User> getAllAdmins() {
        return userService.findAllAdmins();
    }

    @GetMapping("/mentors")
    public List<User> getAllMentors() {
        return userService.findAllMentors();
    }

    @GetMapping("/interns")
    public List<User> getAllInterns() {
        return userService.findAllInterns();
    }

    @GetMapping("{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @GetMapping("{username}")
    public UserCredentialsDTO getUserSecurityDetails (@PathVariable String username) {

        User targetUser = userService.findUserByUsername(username);

        if (targetUser == null) {
            throw new NoSuchUserException("no user with this username was found");
        }

        return new UserCredentialsDTO(username, targetUser.getPassword());
    }

    @PostMapping()
    public User createUser(@RequestBody User newUser) {
        return userService.addUser(newUser);
    }

    @PutMapping("{id}")
    public User updateUserTotally(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.replaceUser(id, updatedUser);
    }

    @PatchMapping("{id}")
    public User updateUserPartially(@PathVariable Long id, @RequestBody User patchPayLoad) {
        return userService.updateUser(id, patchPayLoad);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

}

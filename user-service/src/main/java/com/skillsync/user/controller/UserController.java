package com.skillsync.user.controller;

import com.skillsync.user.entity.User;
import com.skillsync.user.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import org.springframework.cache.annotation.Cacheable;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Cacheable("allUsers")
    @GetMapping
    public List<User> getAllUsers() {
    	System.out.println("DB HIT: getAllUsers");
        return userRepository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @Cacheable("usersByMail")
    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    
//    void demoMethod() {
//    	if(true) {
//        	System.out.println("Issue");
//        }
//    }
}
package com.skillsync.auth.controller;
import com.skillsync.auth.dto.LoginRequest;
import com.skillsync.auth.dto.RegisterRequest;
import com.skillsync.auth.dto.ApiResponse;
import com.skillsync.auth.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // register
    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody RegisterRequest request) {

        Map<String, Object> user = new HashMap<>();
        user.put("name", request.getName());
        user.put("email", request.getEmail());
        user.put("password", passwordEncoder.encode(request.getPassword()));
        user.put("role", request.getRole());

        restTemplate.postForObject(
                "http://user-service/users",
                user,
                String.class
        );

        return new ApiResponse<>(
                "User registered successfully",
                null
        );
    }

    // login
    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginRequest request) {

        Map user = restTemplate.getForObject(
                "http://user-service/users/email/" + request.getEmail(),
                Map.class
        );

        System.out.println("USER RESPONSE: " + user);

        if (user == null) {
        	throw new org.springframework.web.server.ResponseStatusException(
        			org.springframework.http.HttpStatus.BAD_REQUEST,
        	        "User not found"
        	);
        }

        String password = (String) user.get("password");

        if (!passwordEncoder.matches(request.getPassword(), password)) {
        	throw new org.springframework.web.server.ResponseStatusException(
        	        org.springframework.http.HttpStatus.BAD_REQUEST,
        	        "Invalid password"
        	);
        }

        String token = jwtUtil.generateTokenFromMap(user);

        return new ApiResponse<>(
                "Login successful",
                token
        );
    }

    // admin endpoint
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    public ApiResponse<String> adminEndpoint() {

//        System.out.println("Role from Gateway: " + role);

        return new ApiResponse<>(
                "Welcome Admin!",
                "Access Granted!"
        );
    }
}
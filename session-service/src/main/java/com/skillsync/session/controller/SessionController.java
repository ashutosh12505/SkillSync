package com.skillsync.session.controller;

import com.skillsync.session.dto.ApiResponse;
import com.skillsync.session.entity.Session;
import com.skillsync.session.producer.SessionProducer;
import com.skillsync.session.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private SessionProducer producer;

    // 🔥 BOOK SESSION
    @PostMapping
    public ApiResponse<Session> createSession(
            @RequestBody Session session,
            @RequestHeader("X-User-Email") String email
    ) {

        // 🔥 get user from user-service
    	Map user = restTemplate.getForObject(
    	        "http://user-service/users/email/" + email,
    	        Map.class
    	);

    	if (user == null) {
    	    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    	}
    	
    	System.out.println("EMAIL: " + email);

        Number userId = (Number) user.get("id");

        session.setUserId(userId.longValue());
        session.setStatus("BOOKED");
        
        producer.sendMessage("Session booked for mentorId: " + session.getMentorId());

        Session savedSession = sessionService.createSession(session);

        // 🔥 RETURN CLEAN RESPONSE
        return new ApiResponse<>(
                "Session booked successfully",
                savedSession
        );
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<Session>> getUserSessions(@PathVariable Long userId) {

        List<Session> sessions = sessionService.getByUser(userId);

        return new ApiResponse<>(
                "User sessions fetched successfully",
                sessions
        );
    }

    @GetMapping("/mentor/{mentorId}")
    public ApiResponse<List<Session>> getMentorSessions(@PathVariable Long mentorId) {

        List<Session> sessions = sessionService.getByMentor(mentorId);

        return new ApiResponse<>(
                "Mentor sessions fetched successfully",
                sessions
        );
    }
}
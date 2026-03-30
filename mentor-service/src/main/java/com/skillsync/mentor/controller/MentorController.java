package com.skillsync.mentor.controller;

import com.skillsync.mentor.entity.Mentor;
import com.skillsync.mentor.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import org.springframework.cache.annotation.Cacheable;

@RestController
@RequestMapping("/mentors")
public class MentorController {

    @Autowired
    private MentorService mentorService;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping
    public Mentor createMentor(
            @RequestBody Mentor mentor,
            @RequestHeader("X-User-Email") String email
    ) {
    	System.out.println("CREATE MENTOR API CALLED");
		System.out.println("Incoming mentor: " + mentor);
		System.out.println("Email from header: " + email);

        Map user = restTemplate.getForObject(
                "http://user-service/users/email/" + email,
                Map.class
        );

        System.out.println("USER RESPONSE: " + user);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Number userId = (Number) user.get("id");
        mentor.setUserId(userId.longValue());

        Mentor saved = mentorService.createMentor(mentor);

        System.out.println("SAVED MENTOR: " + saved);

        return saved;
    }
    
    @Cacheable("topMentors")
    @GetMapping("/top")
    public List<Mentor> getTopMentors() {

        List<Mentor> mentors = mentorService.getAllMentors();
        List<Mentor> result = new ArrayList<>();

        for (Mentor mentor : mentors) {

            List<Map> reviews = restTemplate.getForObject(
                    "http://review-service/reviews/mentor/" + mentor.getId(),
                    List.class
            );

            if (reviews == null || reviews.isEmpty()) continue;

            double avg = reviews.stream()
            		.mapToInt(r -> ((Number) r.get("rating")).intValue())
                    .average()
                    .orElse(0);

            mentor.setRating(avg);

            result.add(mentor);
        }

        result.sort((a, b) -> Double.compare(b.getRating(), a.getRating()));

        return result;
    }

    @GetMapping
    public java.util.List<Mentor> getAllMentors() {
        return mentorService.getAllMentors();
    }

    @GetMapping("/{id}")
    public Mentor getMentor(@PathVariable Long id) {
        return mentorService.getMentorById(id);
    }

    @GetMapping("/skill/{skill}")
    public java.util.List<Mentor> getBySkill(@PathVariable String skill) {
        return mentorService.getMentorsBySkill(skill);
    }
    
    @GetMapping("/search/{skill}")
    public java.util.List<Mentor> searchBySkill(@PathVariable String skill) {

        java.util.List<Map> skills = restTemplate.getForObject(
                "http://skill-service/skills/name/" + skill,
                java.util.List.class
        );

        System.out.println("Skills response: " + skills);

        if (skills == null || skills.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        java.util.List<Long> mentorIds = new java.util.ArrayList<>();

        for (Map s : skills) {
            Number mentorId = (Number) s.get("mentorId");
            mentorIds.add(mentorId.longValue());
        }

        java.util.List<Mentor> mentors = new java.util.ArrayList<>();

        for (Long id : mentorIds) {
            Mentor mentor = mentorService.getMentorById(id);
            if (mentor != null) {
                mentors.add(mentor);
            }
        }

        return mentors;
    }
}
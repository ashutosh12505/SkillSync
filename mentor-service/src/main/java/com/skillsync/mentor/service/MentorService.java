package com.skillsync.mentor.service;

import com.skillsync.mentor.entity.Mentor;
import com.skillsync.mentor.repository.MentorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.cache.annotation.Cacheable;

@Service
public class MentorService {

    @Autowired
    private MentorRepository mentorRepository;

    public Mentor createMentor(Mentor mentor) {
        return mentorRepository.save(mentor);
    }

    @Cacheable("allMentors")
    public List<Mentor> getAllMentors() {
        return mentorRepository.findAll();
    }

    @Cacheable("searchMentors")
    public Mentor getMentorById(Long id) {
        return mentorRepository.findById(id).orElse(null);
    }

    public List<Mentor> getMentorsBySkill(String skill) {
        return mentorRepository.findByExpertise(skill);
    }
}
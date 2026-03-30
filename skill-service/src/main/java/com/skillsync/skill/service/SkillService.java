package com.skillsync.skill.service;

import com.skillsync.skill.entity.Skill;
import com.skillsync.skill.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.cache.annotation.Cacheable;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public Skill addSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    @Cacheable("allSkills")
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public List<Skill> getByName(String name) {
        return skillRepository.findByName(name);
    }

    public List<Skill> getByMentor(Long mentorId) {
        return skillRepository.findByMentorId(mentorId);
    }
}
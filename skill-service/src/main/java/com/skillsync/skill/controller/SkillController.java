package com.skillsync.skill.controller;

import com.skillsync.skill.entity.Skill;
import com.skillsync.skill.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @PostMapping
    public Skill addSkill(@RequestBody Skill skill) {
        return skillService.addSkill(skill);
    }

    @GetMapping
    public List<Skill> getAll() {
        return skillService.getAllSkills();
    }

    @GetMapping("/name/{name}")
    public List<Skill> getByName(@PathVariable String name) {
        return skillService.getByName(name);
    }

    @GetMapping("/mentor/{mentorId}")
    public List<Skill> getByMentor(@PathVariable Long mentorId) {
        return skillService.getByMentor(mentorId);
    }
}
package com.skillsync.mentor.repository;

import com.skillsync.mentor.entity.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentorRepository extends JpaRepository<Mentor, Long> {

    List<Mentor> findByExpertise(String expertise);
}
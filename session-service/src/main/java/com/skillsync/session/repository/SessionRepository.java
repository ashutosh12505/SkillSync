package com.skillsync.session.repository;

import com.skillsync.session.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByUserId(Long userId);
    List<Session> findByMentorId(Long mentorId);
}
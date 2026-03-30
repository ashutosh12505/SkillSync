package com.skillsync.session.service;

import com.skillsync.session.entity.Session;
import com.skillsync.session.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public Session createSession(Session session) {
        return sessionRepository.save(session);
    }

    public List<Session> getByUser(Long userId) {
        return sessionRepository.findByUserId(userId);
    }

    public List<Session> getByMentor(Long mentorId) {
        return sessionRepository.findByMentorId(mentorId);
    }
}
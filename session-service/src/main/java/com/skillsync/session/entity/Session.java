package com.skillsync.session.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long mentorId;

    private LocalDateTime sessionTime;

    private String status; // BOOKED, COMPLETED, CANCELLED

    public Session() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getMentorId() {
		return mentorId;
	}

	public void setMentorId(Long mentorId) {
		this.mentorId = mentorId;
	}

	public LocalDateTime getSessionTime() {
		return sessionTime;
	}

	public void setSessionTime(LocalDateTime sessionTime) {
		this.sessionTime = sessionTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
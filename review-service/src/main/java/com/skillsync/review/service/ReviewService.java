package com.skillsync.review.service;

import com.skillsync.review.entity.Review;
import com.skillsync.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getByMentor(Long mentorId) {
        return reviewRepository.findByMentorId(mentorId);
    }
}
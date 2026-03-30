package com.skillsync.review.controller;

import com.skillsync.review.entity.Review;
import com.skillsync.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // 🔥 ADD REVIEW
    @PostMapping
    public Review addReview(@RequestBody Review review) {
        return reviewService.addReview(review);
    }

    // 🔥 GET REVIEWS FOR MENTOR
    @GetMapping("/mentor/{mentorId}")
    public List<Review> getReviews(@PathVariable Long mentorId) {
        return reviewService.getByMentor(mentorId);
    }
}
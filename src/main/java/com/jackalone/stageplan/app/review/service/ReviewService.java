package com.jackalone.stageplan.app.review.service;

import com.jackalone.stageplan.app.performance.domain.Performance;
import com.jackalone.stageplan.app.performance.repository.PerformanceRepository;
import com.jackalone.stageplan.app.review.domain.Review;
import com.jackalone.stageplan.app.review.dto.ReviewDto;
import com.jackalone.stageplan.app.review.repository.ReviewRepository;
import com.jackalone.stageplan.app.user.domain.User;
import com.jackalone.stageplan.app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PerformanceRepository performanceRepository;

    public ReviewDto.Response createReview(String userEmail, ReviewDto.CreateRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Performance performance = performanceRepository.findById(request.getPerformanceId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연입니다."));

        Review review = Review.builder()
                .content(request.getContent())
                .rating(request.getRating())
                .user(user)
                .performance(performance)
                .build();

        Review savedReview = reviewRepository.save(review);
        return convertToResponse(savedReview);
    }

    @Transactional(readOnly = true)
    public List<ReviewDto.Response> getReviewsByPerformanceId(Long performanceId) {
        List<Review> reviews = reviewRepository.findByPerformanceIdOrderByCreatedAtDesc(performanceId);
        return reviews.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewDto.Response> getUserReviews(String userEmail) {
        List<Review> reviews = reviewRepository.findByUserEmailOrderByCreatedAtDesc(userEmail);
        return reviews.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ReviewDto.Response updateReview(Long reviewId, String userEmail, ReviewDto.UpdateRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));

        if (!review.getUser().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        if (request.getContent() != null) review.setContent(request.getContent());
        if (request.getRating() != null) review.setRating(request.getRating());

        Review updatedReview = reviewRepository.save(review);
        return convertToResponse(updatedReview);
    }

    public void deleteReview(Long reviewId, String userEmail) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));

        if (!review.getUser().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        reviewRepository.delete(review);
    }

    private ReviewDto.Response convertToResponse(Review review) {
        return ReviewDto.Response.builder()
                .id(review.getId())
                .content(review.getContent())
                .rating(review.getRating())
                .userName(review.getUser().getName())
                .performanceTitle(review.getPerformance().getTitle())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}


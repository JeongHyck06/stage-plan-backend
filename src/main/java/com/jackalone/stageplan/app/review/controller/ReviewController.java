package com.jackalone.stageplan.app.review.controller;

import com.jackalone.stageplan.app.review.dto.ReviewDto;
import com.jackalone.stageplan.app.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "리뷰", description = "리뷰 관련 API")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "리뷰 작성", description = "공연에 대한 리뷰를 작성합니다.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ReviewDto.Response> createReview(
            @Valid @RequestBody ReviewDto.CreateRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        ReviewDto.Response response = reviewService.createReview(userEmail, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/performance/{performanceId}")
    @Operation(summary = "공연별 리뷰 조회", description = "특정 공연의 리뷰를 조회합니다.")
    public ResponseEntity<List<ReviewDto.Response>> getReviewsByPerformanceId(
            @Parameter(description = "공연 ID") @PathVariable Long performanceId) {
        List<ReviewDto.Response> responses = reviewService.getReviewsByPerformanceId(performanceId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/my")
    @Operation(summary = "내 리뷰 조회", description = "내가 작성한 리뷰를 조회합니다.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<ReviewDto.Response>> getMyReviews(Authentication authentication) {
        String userEmail = authentication.getName();
        List<ReviewDto.Response> responses = reviewService.getUserReviews(userEmail);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{reviewId}")
    @Operation(summary = "리뷰 수정", description = "리뷰를 수정합니다.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ReviewDto.Response> updateReview(
            @Parameter(description = "리뷰 ID") @PathVariable Long reviewId,
            @Valid @RequestBody ReviewDto.UpdateRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        ReviewDto.Response response = reviewService.updateReview(reviewId, userEmail, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "리뷰 ID") @PathVariable Long reviewId,
            Authentication authentication) {
        String userEmail = authentication.getName();
        reviewService.deleteReview(reviewId, userEmail);
        return ResponseEntity.ok().build();
    }
}

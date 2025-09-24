package com.jackalone.stageplan.app.performance.controller;

import com.jackalone.stageplan.app.performance.dto.PerformanceDto;
import com.jackalone.stageplan.app.performance.service.PerformanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/performances")
@RequiredArgsConstructor
@Tag(name = "공연", description = "공연 관련 API")
public class PerformanceController {

    private final PerformanceService performanceService;

    @PostMapping
    @Operation(summary = "공연 등록", description = "새로운 공연을 등록합니다.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PerformanceDto.Response> createPerformance(
            @Valid @RequestBody PerformanceDto.CreateRequest request,
            Authentication authentication) {
        try {
            System.out.println("Creating performance for user: " + authentication.getName());
            System.out.println("Request data: " + request.toString());
            
            String userEmail = authentication.getName();
            PerformanceDto.Response response = performanceService.createPerformance(userEmail, request);
            
            System.out.println("Performance created successfully with ID: " + response.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error creating performance: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping
    @Operation(summary = "전체 공연 조회", description = "모든 공연을 조회합니다.")
    public ResponseEntity<List<PerformanceDto.Response>> getAllPerformances() {
        List<PerformanceDto.Response> responses = performanceService.getAllPerformances();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    @Operation(summary = "공연 검색", description = "키워드로 공연을 검색합니다.")
    public ResponseEntity<List<PerformanceDto.Response>> searchPerformances(
            @Parameter(description = "검색 키워드") @RequestParam(required = false) String keyword,
            @Parameter(description = "장르") @RequestParam(required = false) String genre,
            @Parameter(description = "밴드명") @RequestParam(required = false) String bandName) {
        
        PerformanceDto.SearchRequest searchRequest = PerformanceDto.SearchRequest.builder()
                .keyword(keyword)
                .genre(genre)
                .bandName(bandName)
                .build();
        
        List<PerformanceDto.Response> responses = performanceService.searchPerformances(searchRequest);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/calendar")
    @Operation(summary = "캘린더용 공연 조회", description = "특정 기간의 공연을 조회합니다.")
    public ResponseEntity<List<PerformanceDto.Response>> getPerformancesByDateRange(
            @Parameter(description = "시작 날짜") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "종료 날짜") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<PerformanceDto.Response> responses = performanceService.getPerformancesByDateRange(startDate, endDate);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/my")
    @Operation(summary = "내 공연 조회", description = "내가 등록한 공연을 조회합니다.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<PerformanceDto.Response>> getMyPerformances(Authentication authentication) {
        String userEmail = authentication.getName();
        List<PerformanceDto.Response> responses = performanceService.getUserPerformances(userEmail);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{performanceId}")
    @Operation(summary = "공연 수정", description = "공연 정보를 수정합니다.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PerformanceDto.Response> updatePerformance(
            @Parameter(description = "공연 ID") @PathVariable Long performanceId,
            @Valid @RequestBody PerformanceDto.UpdateRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        PerformanceDto.Response response = performanceService.updatePerformance(performanceId, userEmail, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{performanceId}")
    @Operation(summary = "공연 삭제", description = "공연을 삭제합니다.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deletePerformance(
            @Parameter(description = "공연 ID") @PathVariable Long performanceId,
            Authentication authentication) {
        String userEmail = authentication.getName();
        performanceService.deletePerformance(performanceId, userEmail);
        return ResponseEntity.ok().build();
    }
}


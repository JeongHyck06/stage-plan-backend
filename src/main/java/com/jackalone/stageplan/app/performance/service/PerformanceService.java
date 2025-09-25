package com.jackalone.stageplan.app.performance.service;

import com.jackalone.stageplan.app.performance.domain.Performance;
import com.jackalone.stageplan.app.performance.dto.PerformanceDto;
import com.jackalone.stageplan.app.performance.repository.PerformanceRepository;
import com.jackalone.stageplan.app.user.domain.User;
import com.jackalone.stageplan.app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final UserRepository userRepository;

    public PerformanceDto.Response createPerformance(String userEmail, PerformanceDto.CreateRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Performance performance = Performance.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .location(request.getLocation())
                .performanceDate(request.getPerformanceDate())
                .genre(request.getGenre())
                .bandName(request.getBandName())
                .ticketPrice(request.getTicketPrice())
                .maxAudience(request.getMaxAudience())
                .status(Performance.Status.ACTIVE)
                .user(user)
                .build();

        Performance savedPerformance = performanceRepository.save(performance);

        return convertToResponse(savedPerformance);
    }

    @Transactional(readOnly = true)
    public List<PerformanceDto.Response> getAllPerformances() {
        List<Performance> performances = performanceRepository.findAll();
        return performances.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PerformanceDto.Response> searchPerformances(PerformanceDto.SearchRequest request) {
        List<Performance> performances;

        // 검색 조건이 있는 경우 검색 쿼리 사용
        boolean hasSearchConditions = (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) ||
                                    (request.getGenre() != null && !request.getGenre().trim().isEmpty()) ||
                                    (request.getBandName() != null && !request.getBandName().trim().isEmpty());

        if (hasSearchConditions) {
            performances = performanceRepository.searchPerformances(
                request.getKeyword(),
                request.getGenre(),
                request.getBandName()
            );
        } else {
            // 검색 조건이 없으면 모든 공연 조회
            performances = performanceRepository.findAll();
        }

        return performances.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PerformanceDto.Response> getPerformancesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Performance> performances = performanceRepository
                .findByPerformanceDateBetweenAndStatus(startDate, endDate, Performance.Status.ACTIVE);
        
        return performances.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PerformanceDto.Response> getUserPerformances(String userEmail) {
        List<Performance> performances = performanceRepository
                .findByUserEmailAndStatus(userEmail, Performance.Status.ACTIVE);
        
        return performances.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public PerformanceDto.Response updatePerformance(Long performanceId, String userEmail, PerformanceDto.UpdateRequest request) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연입니다."));

        if (!performance.getUser().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        if (request.getTitle() != null) performance.setTitle(request.getTitle());
        if (request.getContent() != null) performance.setContent(request.getContent());
        if (request.getLocation() != null) performance.setLocation(request.getLocation());
        if (request.getPerformanceDate() != null) performance.setPerformanceDate(request.getPerformanceDate());
        if (request.getGenre() != null) performance.setGenre(request.getGenre());
        if (request.getBandName() != null) performance.setBandName(request.getBandName());
        if (request.getTicketPrice() != null) performance.setTicketPrice(request.getTicketPrice());
        if (request.getMaxAudience() != null) performance.setMaxAudience(request.getMaxAudience());
        if (request.getStatus() != null) performance.setStatus(request.getStatus());

        Performance updatedPerformance = performanceRepository.save(performance);
        return convertToResponse(updatedPerformance);
    }

    public void deletePerformance(Long performanceId, String userEmail) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연입니다."));

        if (!performance.getUser().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        performanceRepository.delete(performance);
    }

    private PerformanceDto.Response convertToResponse(Performance performance) {
        return PerformanceDto.Response.builder()
                .id(performance.getId())
                .title(performance.getTitle())
                .content(performance.getContent())
                .location(performance.getLocation())
                .performanceDate(performance.getPerformanceDate())
                .genre(performance.getGenre())
                .bandName(performance.getBandName())
                .ticketPrice(performance.getTicketPrice())
                .maxAudience(performance.getMaxAudience())
                .status(performance.getStatus())
                .artistName(performance.getUser().getName())
                .createdAt(performance.getCreatedAt())
                .updatedAt(performance.getUpdatedAt())
                .build();
    }
}


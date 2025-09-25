package com.jackalone.stageplan.app.performance.dto;

import com.jackalone.stageplan.app.performance.domain.Performance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class PerformanceDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "제목은 필수입니다.")
        private String title;

        private String content;

        @NotBlank(message = "위치는 필수입니다.")
        private String location;

        @NotNull(message = "공연 날짜는 필수입니다.")
        private LocalDateTime performanceDate;

        private String genre;

        private String bandName;

        private Integer ticketPrice;

        private Integer maxAudience;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String title;
        private String content;
        private String location;
        private LocalDateTime performanceDate;
        private String genre;
        private String bandName;
        private Integer ticketPrice;
        private Integer maxAudience;
        private Performance.Status status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private String location;
        private LocalDateTime performanceDate;
        private String genre;
        private String bandName;
        private Integer ticketPrice;
        private Integer maxAudience;
        private Performance.Status status;
        private String artistName;
        private Long artistId;
        private String artistNickname;
        private String artistProfileImageUrl;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchRequest {
        private String keyword;
        private String genre;
        private String bandName;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }
}

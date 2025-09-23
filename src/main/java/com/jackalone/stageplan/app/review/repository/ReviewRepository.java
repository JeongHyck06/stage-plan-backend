package com.jackalone.stageplan.app.review.repository;

import com.jackalone.stageplan.app.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByPerformanceIdOrderByCreatedAtDesc(Long performanceId);
    
    @Query("SELECT r FROM Review r JOIN r.user u WHERE u.email = :email ORDER BY r.createdAt DESC")
    List<Review> findByUserEmailOrderByCreatedAtDesc(@Param("email") String email);
    
    @Query("SELECT r FROM Review r JOIN r.user u JOIN r.performance p WHERE p.id = :performanceId AND u.email = :email")
    List<Review> findByPerformanceIdAndUserEmail(@Param("performanceId") Long performanceId, @Param("email") String email);
}

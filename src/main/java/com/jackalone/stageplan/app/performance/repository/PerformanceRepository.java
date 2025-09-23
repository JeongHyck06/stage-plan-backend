package com.jackalone.stageplan.app.performance.repository;

import com.jackalone.stageplan.app.performance.domain.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    
    List<Performance> findByGenreContainingIgnoreCase(String genre);
    
    List<Performance> findByBandNameContainingIgnoreCase(String bandName);
    
    List<Performance> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT p FROM Performance p WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.bandName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.genre) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "p.status = 'ACTIVE'")
    List<Performance> searchPerformances(@Param("keyword") String keyword);
    
    List<Performance> findByPerformanceDateBetweenAndStatus(
        LocalDateTime startDate, 
        LocalDateTime endDate, 
        Performance.Status status
    );
    
    @Query("SELECT p FROM Performance p JOIN p.user u WHERE u.email = :email AND p.status = :status")
    List<Performance> findByUserEmailAndStatus(@Param("email") String email, @Param("status") Performance.Status status);
}

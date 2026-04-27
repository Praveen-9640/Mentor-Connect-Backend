package com.klu.repository;

import com.klu.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

    @Query("SELECT s FROM Session s WHERE (s.mentee.id = :menteeId OR s.mentor.id = :mentorId) " +
           "AND (" +
           "(s.startTime < :endTime AND s.endTime > :startTime)" +
           ")")
    List<Session> findOverlappingSessions(@Param("menteeId") Long menteeId, 
                                          @Param("mentorId") Long mentorId, 
                                          @Param("startTime") LocalDateTime startTime, 
                                          @Param("endTime") LocalDateTime endTime);
}
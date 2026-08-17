package com.klu.repository;

import com.klu.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    @Query("""
           SELECT s
           FROM Session s
           JOIN FETCH s.mentor
           JOIN FETCH s.mentee
           """)
    List<Session> findAllWithUsers();

    @Query("""
           SELECT s
           FROM Session s
           JOIN FETCH s.mentor
           JOIN FETCH s.mentee
           WHERE s.mentor.id = :mentorId
           """)
    List<Session> findByMentorId(@Param("mentorId") Long mentorId);

    @Query("""
           SELECT s
           FROM Session s
           JOIN FETCH s.mentor
           JOIN FETCH s.mentee
           WHERE s.mentee.id = :menteeId
           """)
    List<Session> findByMenteeId(@Param("menteeId") Long menteeId);

    @Query("""
           SELECT s
           FROM Session s
           JOIN FETCH s.mentor
           JOIN FETCH s.mentee
           WHERE s.id = :id
           """)
    Optional<Session> findByIdWithUsers(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Session s SET s.status = :status WHERE s.id = :id")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Modifying
    @Transactional
    @Query("DELETE FROM Session s WHERE s.mentee.id = :id OR s.mentor.id = :id")
    int deleteAllByUser(@Param("id") Long id);

    @Query("SELECT s FROM Session s WHERE (s.mentee.id = :menteeId OR s.mentor.id = :mentorId) " +
           "AND (" +
           "(s.startTime < :endTime AND s.endTime > :startTime)" +
           ")")
    List<Session> findOverlappingSessions(@Param("menteeId") Long menteeId, 
                                          @Param("mentorId") Long mentorId, 
                                          @Param("startTime") LocalDateTime startTime, 
                                          @Param("endTime") LocalDateTime endTime);
}
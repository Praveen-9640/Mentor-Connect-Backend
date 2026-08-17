package com.klu.service;

import com.klu.entity.Session;
import com.klu.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionService {

    @Autowired
    private SessionRepository repo;

    public Session book(Session session) {

        if (session.getMentee() == null || session.getMentee().getId() == null
                || session.getMentor() == null || session.getMentor().getId() == null) {
            throw new RuntimeException("Mentee and mentor are required.");
        }

        if (session.getStartTime() == null || session.getEndTime() == null) {
            throw new RuntimeException("Start and end times are required.");
        }

        if (!session.getStartTime().isBefore(session.getEndTime())) {
            throw new RuntimeException("Start time must be before end time.");
        }

        if (session.getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot book a session in the past.");
        }

        List<Session> overlapping = repo.findOverlappingSessions(
                session.getMentee().getId(),
                session.getMentor().getId(),
                session.getStartTime(),
                session.getEndTime()
        );

        if (!overlapping.isEmpty()) {
            throw new RuntimeException("This time slot is already booked for either the mentee or mentor.");
        }

        session.setStatus("BOOKED");
        return repo.save(session);
    }

    public List<Session> getAll() {
        return repo.findAllWithUsers();
    }

    public List<Session> getMentorSessions(Long mentorId) {
        return repo.findByMentorId(mentorId);
    }

    public List<Session> getMenteeSessions(Long menteeId) {
        return repo.findByMenteeId(menteeId);
    }

    public Session findById(Long id) {
        return repo.findByIdWithUsers(id).orElseThrow(() -> new RuntimeException("Session not found"));
    }

    @Transactional
    public Session updateStatus(Long id, String status) {
        int updatedRows = repo.updateStatus(id, status);
        if (updatedRows == 0) {
            throw new RuntimeException("Session not found");
        }

        return repo.findByIdWithUsers(id).orElseThrow(() -> new RuntimeException("Session not found"));
    }
}

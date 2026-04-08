package com.klu.service;

import com.klu.entity.Session;
import com.klu.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {

    @Autowired
    private SessionRepository repo;

    public Session book(Session session) {
        session.setStatus("BOOKED");
        return repo.save(session);
    }

    public List<Session> getAll() {
        return repo.findAll();
    }

    public Session updateStatus(Long id, String status) {
        Session session = repo.findById(id).orElseThrow(() -> new RuntimeException("Session not found"));
        session.setStatus(status);
        return repo.save(session);
    }
}
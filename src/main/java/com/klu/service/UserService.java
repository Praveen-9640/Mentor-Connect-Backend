package com.klu.service;

import com.klu.entity.User;
import com.klu.entity.Session;
import com.klu.repository.SessionRepository;
import com.klu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private SessionRepository sessionRepo;

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public void deleteUser(Long id) {
        // Delete all sessions associated with this user first to prevent foreign key errors
        List<Session> sessions = sessionRepo.findAll();
        for (Session session : sessions) {
            if ((session.getMentee() != null && session.getMentee().getId().equals(id)) ||
                (session.getMentor() != null && session.getMentor().getId().equals(id))) {
                sessionRepo.delete(session);
            }
        }
        repo.deleteById(id);
    }
}
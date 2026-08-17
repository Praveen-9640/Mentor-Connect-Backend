package com.klu.service;

import com.klu.dto.StatsDTO;
import com.klu.entity.Role;
import com.klu.entity.User;
import com.klu.repository.SessionRepository;
import com.klu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private SessionRepository sessionRepo;

    public List<User> getUsers(String role) {
        if (role == null || role.isBlank()) {
            return repo.findAll();
        }

        try {
            return repo.findByRole(Role.valueOf(role.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }

    public StatsDTO getStats() {
        StatsDTO stats = new StatsDTO();
        stats.setMentors(repo.countByRole(Role.MENTOR));
        stats.setMentees(repo.countByRole(Role.MENTEE));
        stats.setTopics(repo.countDistinctTopics());
        return stats;
    }

    @Transactional
    public void deleteUser(Long id) {
        sessionRepo.deleteAllByUser(id);
        repo.deleteById(id);
    }
}

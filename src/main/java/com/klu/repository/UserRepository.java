package com.klu.repository;

import com.klu.entity.Role;
import com.klu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    long countByRole(Role role);

    @Query("SELECT COUNT(DISTINCT LOWER(TRIM(u.subject))) FROM User u " +
            "WHERE u.role = com.klu.entity.Role.MENTOR AND u.subject IS NOT NULL AND u.subject <> ''")
    long countDistinctTopics();
}

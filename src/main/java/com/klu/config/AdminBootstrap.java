package com.klu.config;

import com.klu.entity.Role;
import com.klu.entity.User;
import com.klu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminBootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    public AdminBootstrap(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        User admin = userRepository.findByEmail(adminEmail).orElseGet(User::new);
        boolean needsUpdate = admin.getId() == null
                || admin.getRole() != Role.ADMIN
                || !passwordEncoder.matches(adminPassword, admin.getPassword() == null ? "" : admin.getPassword())
                || !"Praveen".equals(admin.getName());

        if (!needsUpdate) {
            return;
        }

        admin.setName("Praveen");
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);
        admin.setSubject(null);
        admin.setStudyYear(null);

        userRepository.save(admin);
    }
}

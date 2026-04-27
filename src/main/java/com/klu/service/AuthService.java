package com.klu.service;

import com.klu.dto.LoginRequest;
import com.klu.dto.RegisterRequest;
import com.klu.entity.Role;
import com.klu.entity.User;
import com.klu.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;


    public String register(RegisterRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setSubject(request.getSubject());
        user.setStudyYear(request.getStudyYear());


        System.out.println("Incoming Role: " + request.getRole());


        if (request.getRole() == null || request.getRole().isEmpty()) {
            throw new RuntimeException("Role is required");
        }

        try {
            user.setRole(Role.valueOf(request.getRole().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role. Use ADMIN, MENTOR, MENTEE");
        }

        userRepository.save(user);

        return "User Registered Successfully";
    }


    public User login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }


        new Thread(() -> {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(user.getEmail());
                message.setSubject("Login Alert - MentorConnect");

                String roleSpecificText = "";
                if (user.getRole() == Role.MENTOR) {
                    roleSpecificText = "hope you find the perfect mentee to guide";
                } else if (user.getRole() == Role.MENTEE) {
                    roleSpecificText = "hope you have found the perfect mentor";
                } else {
                    roleSpecificText = "hope you have a great experience";
                }

                message.setText("Hello " + user.getName() + ",\n\n" +
                        "you have logined into Mentor Connect " + roleSpecificText + " \n" +
                        "from the team Mentor Connect");
                mailSender.send(message);
            } catch (Exception e) {
                System.out.println("Failed to send email: " + e.getMessage());
            }
        }).start();

        return user;
    }
}
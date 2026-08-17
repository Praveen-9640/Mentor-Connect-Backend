package com.klu.service;

import com.klu.entity.Role;
import com.klu.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private final String mailFrom;
    private final RestClient restClient;

    public EmailService(@Value("${RESEND_API_KEY}") String resendApiKey,
                        @Value("${MAIL_FROM}") String mailFrom) {
        this.mailFrom = mailFrom;
        this.restClient = RestClient.builder()
                .baseUrl("https://api.resend.com")
                .defaultHeader("Authorization", "Bearer " + resendApiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Async
    public void sendLoginAlert(User user) {
        try {
            String roleSpecificText;
            if (user.getRole() == Role.MENTOR) {
                roleSpecificText = "hope you find the perfect mentee to guide";
            } else if (user.getRole() == Role.MENTEE) {
                roleSpecificText = "hope you have found the perfect mentor";
            } else {
                roleSpecificText = "hope you have a great experience";
            }

            String body = "Hello " + user.getName() + ",\n\n" +
                    "you have logined into Mentor Connect " + roleSpecificText + " \n" +
                    "from the team Mentor Connect";

            Map<String, Object> request = Map.of(
                    "from", mailFrom,
                    "to", List.of(user.getEmail()),
                    "subject", "Login Alert - MentorConnect",
                    "text", body
            );

            restClient.post()
                    .uri("/emails")
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            System.out.println("Failed to send login email: " + e.getMessage());
        }
    }
}

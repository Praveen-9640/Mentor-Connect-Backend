package com.klu.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String status;

    @ManyToOne
    @JoinColumn(name = "mentee_id", referencedColumnName = "id")
    private User mentee;

    @ManyToOne
    @JoinColumn(name = "mentor_id", referencedColumnName = "id")
    private User mentor;

    // Constructors
    public Session() {
    }

    public Session(LocalDateTime startTime, LocalDateTime endTime, String status, User mentee, User mentor) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.mentee = mentee;
        this.mentor = mentor;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getMentee() {
        return mentee;
    }

    public void setMentee(User mentee) {
        this.mentee = mentee;
    }

    public User getMentor() {
        return mentor;
    }

    public void setMentor(User mentor) {
        this.mentor = mentor;
    }
}
package com.photography.backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ContactSubmission {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private LocalDate eventDate;
    private String message;
    private LocalDateTime createdAt;

    public ContactSubmission() {
    }

    public ContactSubmission(Long id, String name, String email, String phone, LocalDate eventDate, String message, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.eventDate = eventDate;
        this.message = message;
        this.createdAt = createdAt;
    }

    public ContactSubmission(String name, String email, String phone, LocalDate eventDate, String message) {
        this(null, name, email, phone, eventDate, message, LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

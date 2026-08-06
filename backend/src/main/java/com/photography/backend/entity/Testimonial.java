package com.photography.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "testimonials")
public class Testimonial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "couple_names", nullable = false)
    private String coupleNames;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "display_order")
    private Integer displayOrder;

    public Testimonial() {
    }

    public Testimonial(String coupleNames, String message, Integer displayOrder) {
        this.coupleNames = coupleNames;
        this.message = message;
        this.displayOrder = displayOrder != null ? displayOrder : 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoupleNames() {
        return coupleNames;
    }

    public void setCoupleNames(String coupleNames) {
        this.coupleNames = coupleNames;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}

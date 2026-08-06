package com.photography.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cloudinary_url", nullable = false, length = 1000)
    private String cloudinaryUrl;

    @Column(name = "cloudinary_public_id")
    private String cloudinaryPublicId;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "alt_text")
    private String altText;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @Column(name = "hero_order")
    private Integer heroOrder = 0;

    @Column(name = "selected_works_order")
    private Integer selectedWorksOrder = 0;

    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured = false;

    @Column(name = "show_in_hero", nullable = false)
    private Boolean showInHero = false;

    @Column(name = "show_in_selected_works", nullable = false)
    private Boolean showInSelectedWorks = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Image() {
    }

    public Image(String cloudinaryUrl, String cloudinaryPublicId, String category, String altText,
                 Integer displayOrder, Boolean isFeatured, Boolean showInHero, Boolean showInSelectedWorks,
                 Integer heroOrder, Integer selectedWorksOrder) {
        this.cloudinaryUrl = cloudinaryUrl;
        this.cloudinaryPublicId = cloudinaryPublicId;
        this.category = category;
        this.altText = altText;
        this.displayOrder = displayOrder != null ? displayOrder : 0;
        this.isFeatured = isFeatured != null ? isFeatured : false;
        this.showInHero = showInHero != null ? showInHero : false;
        this.showInSelectedWorks = showInSelectedWorks != null ? showInSelectedWorks : false;
        this.heroOrder = heroOrder != null ? heroOrder : 0;
        this.selectedWorksOrder = selectedWorksOrder != null ? selectedWorksOrder : 0;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCloudinaryUrl() {
        return cloudinaryUrl;
    }

    public void setCloudinaryUrl(String cloudinaryUrl) {
        this.cloudinaryUrl = cloudinaryUrl;
    }

    public String getCloudinaryPublicId() {
        return cloudinaryPublicId;
    }

    public void setCloudinaryPublicId(String cloudinaryPublicId) {
        this.cloudinaryPublicId = cloudinaryPublicId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getHeroOrder() {
        return heroOrder;
    }

    public void setHeroOrder(Integer heroOrder) {
        this.heroOrder = heroOrder;
    }

    public Integer getSelectedWorksOrder() {
        return selectedWorksOrder;
    }

    public void setSelectedWorksOrder(Integer selectedWorksOrder) {
        this.selectedWorksOrder = selectedWorksOrder;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Boolean getShowInHero() {
        return showInHero;
    }

    public void setShowInHero(Boolean showInHero) {
        this.showInHero = showInHero;
    }

    public Boolean getShowInSelectedWorks() {
        return showInSelectedWorks;
    }

    public void setShowInSelectedWorks(Boolean showInSelectedWorks) {
        this.showInSelectedWorks = showInSelectedWorks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

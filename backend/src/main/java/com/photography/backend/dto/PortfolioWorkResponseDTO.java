package com.photography.backend.dto;

import com.photography.backend.entity.PortfolioWork;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PortfolioWorkResponseDTO {
    private Long id;
    private String title;
    private String slug;
    private String description;
    private String coverImageUrl;
    private String coverImagePublicId;
    private String category;
    private Long categoryId;
    private String categorySlug;
    private Boolean isPublished;
    private Boolean isFeatured;
    private Boolean showInHero;
    private Boolean showInSelectedWorks;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<WorkImageResponseDTO> images = new ArrayList<>();

    public PortfolioWorkResponseDTO() {
    }

    public PortfolioWorkResponseDTO(PortfolioWork work) {
        if (work != null) {
            this.id = work.getId();
            this.title = work.getTitle();
            this.slug = work.getSlug();
            this.description = work.getDescription();
            this.coverImageUrl = work.getCoverImageUrl();
            this.coverImagePublicId = work.getCoverImagePublicId();
            if (work.getCategory() != null) {
                this.category = work.getCategory().getName();
                this.categoryId = work.getCategory().getId();
                this.categorySlug = work.getCategory().getSlug();
            }
            this.isPublished = work.getIsPublished();
            this.isFeatured = work.getIsFeatured();
            this.showInHero = work.getShowInHero();
            this.showInSelectedWorks = work.getShowInSelectedWorks();
            this.displayOrder = work.getDisplayOrder();
            this.createdAt = work.getCreatedAt();
            this.updatedAt = work.getUpdatedAt();
            if (work.getImages() != null) {
                this.images = work.getImages().stream()
                        .map(WorkImageResponseDTO::new)
                        .collect(Collectors.toList());
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getCoverImagePublicId() {
        return coverImagePublicId;
    }

    public void setCoverImagePublicId(String coverImagePublicId) {
        this.coverImagePublicId = coverImagePublicId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategorySlug() {
        return categorySlug;
    }

    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
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

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<WorkImageResponseDTO> getImages() {
        return images;
    }

    public void setImages(List<WorkImageResponseDTO> images) {
        this.images = images;
    }
}

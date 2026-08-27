package com.photography.backend.dto;

import com.photography.backend.entity.WorkImage;

public class WorkImageResponseDTO {
    private Long id;
    private String imageUrl;
    private String cloudinaryPublicId;
    private String altText;
    private Integer displayOrder;

    public WorkImageResponseDTO() {
    }

    public WorkImageResponseDTO(WorkImage image) {
        if (image != null) {
            this.id = image.getId();
            this.imageUrl = image.getImageUrl();
            this.cloudinaryPublicId = image.getCloudinaryPublicId();
            this.altText = image.getAltText();
            this.displayOrder = image.getDisplayOrder();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCloudinaryPublicId() {
        return cloudinaryPublicId;
    }

    public void setCloudinaryPublicId(String cloudinaryPublicId) {
        this.cloudinaryPublicId = cloudinaryPublicId;
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
}

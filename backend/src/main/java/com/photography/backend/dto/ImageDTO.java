package com.photography.backend.dto;

import com.photography.backend.entity.Image;

public class ImageDTO {
    private Long id;
    private String url;
    private String cloudinaryPublicId;
    private String category;
    private String altText;
    private Integer displayOrder;
    private Integer heroOrder;
    private Integer selectedWorksOrder;
    private Boolean isFeatured;
    private Boolean showInHero;
    private Boolean showInSelectedWorks;

    public ImageDTO() {
    }

    public ImageDTO(Image image) {
        this.id = image.getId();
        this.url = image.getCloudinaryUrl();
        this.cloudinaryPublicId = image.getCloudinaryPublicId();
        this.category = image.getCategory();
        this.altText = image.getAltText();
        this.displayOrder = image.getDisplayOrder();
        this.heroOrder = image.getHeroOrder();
        this.selectedWorksOrder = image.getSelectedWorksOrder();
        this.isFeatured = image.getIsFeatured();
        this.showInHero = image.getShowInHero();
        this.showInSelectedWorks = image.getShowInSelectedWorks();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}

package com.photography.backend.dto;

public class ImageUpdateDTO {
    private String category;
    private String altText;
    private Integer displayOrder;
    private Integer heroOrder;
    private Integer selectedWorksOrder;
    private Boolean isFeatured;
    private Boolean showInHero;
    private Boolean showInSelectedWorks;

    public ImageUpdateDTO() {
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

package com.photography.backend.entity;

public class Image {
    private Long id;
    private String cloudinaryUrl;
    private String cloudinaryPublicId;
    private String category;
    private String altText;
    private Integer displayOrder;
    private Boolean isFeatured;
    private Boolean showInHero;
    private Boolean showInSelectedWorks;
    private Integer heroOrder;
    private Integer selectedWorksOrder;

    public Image() {
    }

    public Image(Long id, String cloudinaryUrl, String cloudinaryPublicId, String category, String altText,
                 Integer displayOrder, Boolean isFeatured, Boolean showInHero, Boolean showInSelectedWorks,
                 Integer heroOrder, Integer selectedWorksOrder) {
        this.id = id;
        this.cloudinaryUrl = cloudinaryUrl;
        this.cloudinaryPublicId = cloudinaryPublicId;
        this.category = category;
        this.altText = altText;
        this.displayOrder = displayOrder;
        this.isFeatured = isFeatured;
        this.showInHero = showInHero;
        this.showInSelectedWorks = showInSelectedWorks;
        this.heroOrder = heroOrder;
        this.selectedWorksOrder = selectedWorksOrder;
    }

    public Image(String cloudinaryUrl, String cloudinaryPublicId, String category, String altText,
                 Integer displayOrder, Boolean isFeatured, Boolean showInHero, Boolean showInSelectedWorks,
                 Integer heroOrder, Integer selectedWorksOrder) {
        this(null, cloudinaryUrl, cloudinaryPublicId, category, altText, displayOrder, isFeatured, showInHero, showInSelectedWorks, heroOrder, selectedWorksOrder);
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
}

package com.photography.backend.dto;

public class CategorySummaryDTO {
    private String name;
    private String slug;
    private String coverImageUrl;
    private int imageCount;

    public CategorySummaryDTO() {
    }

    public CategorySummaryDTO(String name, String slug, String coverImageUrl, int imageCount) {
        this.name = name;
        this.slug = slug;
        this.coverImageUrl = coverImageUrl;
        this.imageCount = imageCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }
}

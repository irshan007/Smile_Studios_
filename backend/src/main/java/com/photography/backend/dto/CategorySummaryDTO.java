package com.photography.backend.dto;

import com.photography.backend.entity.Category;

public class CategorySummaryDTO {
    private Long id;
    private String name;
    private String slug;
    private Integer displayOrder;
    private String coverImageUrl;
    private int imageCount;

    public CategorySummaryDTO() {
    }

    public CategorySummaryDTO(Long id, String name, String slug, Integer displayOrder, String coverImageUrl, int imageCount) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.displayOrder = displayOrder;
        this.coverImageUrl = coverImageUrl;
        this.imageCount = imageCount;
    }

    public CategorySummaryDTO(String name, String slug, String coverImageUrl, int imageCount) {
        this.name = name;
        this.slug = slug;
        this.coverImageUrl = coverImageUrl;
        this.imageCount = imageCount;
    }

    public CategorySummaryDTO(Category category, String coverImageUrl, int imageCount) {
        this.id = category.getId();
        this.name = category.getName();
        this.slug = category.getSlug();
        this.displayOrder = category.getDisplayOrder();
        this.coverImageUrl = coverImageUrl;
        this.imageCount = imageCount;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
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

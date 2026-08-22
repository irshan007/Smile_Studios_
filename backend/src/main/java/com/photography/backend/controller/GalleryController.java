package com.photography.backend.controller;

import com.photography.backend.dto.ApiResponseDTO;
import com.photography.backend.dto.CategorySummaryDTO;
import com.photography.backend.dto.ImageDTO;
import com.photography.backend.service.GalleryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gallery")
public class GalleryController {

    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @GetMapping("/categories")
    public ApiResponseDTO<List<CategorySummaryDTO>> getCategories() {
        List<CategorySummaryDTO> categories = galleryService.getCategories();
        return ApiResponseDTO.ok("Categories retrieved successfully", categories);
    }

    @GetMapping("/hero")
    public ApiResponseDTO<List<ImageDTO>> getHeroImages() {
        List<ImageDTO> heroImages = galleryService.getHeroImages();
        return ApiResponseDTO.ok("Hero slideshow images retrieved successfully", heroImages);
    }

    @GetMapping("/selected-works")
    public ApiResponseDTO<List<ImageDTO>> getSelectedWorksImages() {
        List<ImageDTO> selectedWorks = galleryService.getSelectedWorksImages();
        return ApiResponseDTO.ok("Selected works images retrieved successfully", selectedWorks);
    }

    @GetMapping("/{category}")
    public ApiResponseDTO<List<ImageDTO>> getCategoryImages(@PathVariable String category) {
        List<ImageDTO> images = galleryService.getImagesByCategory(category);
        return ApiResponseDTO.ok("Category images retrieved successfully", images);
    }
}

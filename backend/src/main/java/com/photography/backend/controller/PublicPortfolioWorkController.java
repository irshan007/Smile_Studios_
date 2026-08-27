package com.photography.backend.controller;

import com.photography.backend.dto.ApiResponseDTO;
import com.photography.backend.dto.PortfolioWorkResponseDTO;
import com.photography.backend.service.PortfolioWorkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/works")
public class PublicPortfolioWorkController {

    private final PortfolioWorkService portfolioWorkService;

    public PublicPortfolioWorkController(PortfolioWorkService portfolioWorkService) {
        this.portfolioWorkService = portfolioWorkService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<PortfolioWorkResponseDTO>>> getPublishedWorks(
            @RequestParam(value = "category", required = false) String categorySlug,
            @RequestParam(value = "featured", required = false) Boolean featured
    ) {
        List<PortfolioWorkResponseDTO> works = portfolioWorkService.getPublishedWorks(categorySlug, featured);
        return ResponseEntity.ok(ApiResponseDTO.ok("Published portfolio works retrieved successfully", works));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponseDTO<PortfolioWorkResponseDTO>> getPublishedWorkBySlug(
            @PathVariable String slug
    ) {
        PortfolioWorkResponseDTO work = portfolioWorkService.getPublishedWorkBySlug(slug);
        return ResponseEntity.ok(ApiResponseDTO.ok("Portfolio work retrieved successfully", work));
    }
}

package com.photography.backend.controller;

import com.photography.backend.dto.ApiResponseDTO;
import com.photography.backend.dto.CreatePortfolioWorkRequest;
import com.photography.backend.dto.PortfolioWorkResponseDTO;
import com.photography.backend.dto.UpdatePortfolioWorkRequest;
import com.photography.backend.exception.UnauthorizedException;
import com.photography.backend.service.PortfolioWorkService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/works")
public class AdminPortfolioWorkController {

    private final PortfolioWorkService portfolioWorkService;

    @Value("${admin.api-key:default_secret_key_change_me}")
    private String adminApiKey;

    public AdminPortfolioWorkController(PortfolioWorkService portfolioWorkService) {
        this.portfolioWorkService = portfolioWorkService;
    }

    private void validateAdminKey(HttpServletRequest request) {
        String providedKey = request.getHeader("ADMIN_API_KEY");
        if (providedKey == null || !providedKey.equals(adminApiKey)) {
            throw new UnauthorizedException("Invalid or missing ADMIN_API_KEY header");
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDTO<PortfolioWorkResponseDTO>> createWork(
            HttpServletRequest request,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "categorySlug", required = false) String categorySlug,
            @RequestParam("coverImage") MultipartFile coverImage,
            @RequestParam(value = "galleryImages", required = false) List<MultipartFile> galleryImages,
            @RequestParam(value = "isPublished", required = false, defaultValue = "false") Boolean isPublished,
            @RequestParam(value = "isFeatured", required = false, defaultValue = "false") Boolean isFeatured,
            @RequestParam(value = "showInHero", required = false, defaultValue = "false") Boolean showInHero,
            @RequestParam(value = "showInSelectedWorks", required = false, defaultValue = "false") Boolean showInSelectedWorks,
            @RequestParam(value = "displayOrder", required = false, defaultValue = "0") Integer displayOrder
    ) {
        validateAdminKey(request);

        CreatePortfolioWorkRequest req = new CreatePortfolioWorkRequest();
        req.setTitle(title);
        req.setDescription(description);
        req.setCategoryId(categoryId);
        req.setCategoryName(category);
        req.setCategorySlug(categorySlug);
        req.setIsPublished(isPublished);
        req.setIsFeatured(isFeatured);
        req.setShowInHero(showInHero);
        req.setShowInSelectedWorks(showInSelectedWorks);
        req.setDisplayOrder(displayOrder);

        PortfolioWorkResponseDTO created = portfolioWorkService.createWork(req, coverImage, galleryImages);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Portfolio work created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<PortfolioWorkResponseDTO>>> getAllWorks(HttpServletRequest request) {
        validateAdminKey(request);
        List<PortfolioWorkResponseDTO> works = portfolioWorkService.getAllWorksForAdmin();
        return ResponseEntity.ok(ApiResponseDTO.ok("All portfolio works retrieved successfully", works));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<PortfolioWorkResponseDTO>> getWorkById(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        validateAdminKey(request);
        PortfolioWorkResponseDTO work = portfolioWorkService.getWorkById(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Portfolio work retrieved successfully", work));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDTO<PortfolioWorkResponseDTO>> updateWork(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "categorySlug", required = false) String categorySlug,
            @RequestParam(value = "coverImage", required = false) MultipartFile coverImage,
            @RequestParam(value = "galleryImages", required = false) List<MultipartFile> galleryImages,
            @RequestParam(value = "isPublished", required = false) Boolean isPublished,
            @RequestParam(value = "isFeatured", required = false) Boolean isFeatured,
            @RequestParam(value = "showInHero", required = false) Boolean showInHero,
            @RequestParam(value = "showInSelectedWorks", required = false) Boolean showInSelectedWorks,
            @RequestParam(value = "displayOrder", required = false) Integer displayOrder
    ) {
        validateAdminKey(request);

        UpdatePortfolioWorkRequest req = new UpdatePortfolioWorkRequest();
        req.setTitle(title);
        req.setDescription(description);
        req.setCategoryId(categoryId);
        req.setCategoryName(category);
        req.setCategorySlug(categorySlug);
        req.setIsPublished(isPublished);
        req.setIsFeatured(isFeatured);
        req.setShowInHero(showInHero);
        req.setShowInSelectedWorks(showInSelectedWorks);
        req.setDisplayOrder(displayOrder);

        PortfolioWorkResponseDTO updated = portfolioWorkService.updateWork(id, req, coverImage, galleryImages);
        return ResponseEntity.ok(ApiResponseDTO.ok("Portfolio work updated successfully", updated));
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<ApiResponseDTO<PortfolioWorkResponseDTO>> setPublishStatus(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestParam(value = "published", required = false) Boolean published,
            @RequestParam(value = "isPublished", required = false) Boolean isPublished
    ) {
        validateAdminKey(request);
        boolean status = published != null ? published : (isPublished != null ? isPublished : true);
        PortfolioWorkResponseDTO updated = portfolioWorkService.setPublishedStatus(id, status);
        return ResponseEntity.ok(ApiResponseDTO.ok("Portfolio work publish status updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteWork(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        validateAdminKey(request);
        portfolioWorkService.deleteWork(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Portfolio work deleted successfully", null));
    }

    @DeleteMapping("/{id}/images/{imageId}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteGalleryImage(
            HttpServletRequest request,
            @PathVariable Long id,
            @PathVariable Long imageId
    ) {
        validateAdminKey(request);
        portfolioWorkService.deleteGalleryImage(id, imageId);
        return ResponseEntity.ok(ApiResponseDTO.ok("Gallery image deleted successfully", null));
    }
}

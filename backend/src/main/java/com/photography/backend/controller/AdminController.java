package com.photography.backend.controller;

import com.photography.backend.dto.ApiResponseDTO;
import com.photography.backend.dto.ImageDTO;
import com.photography.backend.dto.ImageUpdateDTO;
import com.photography.backend.entity.Image;
import com.photography.backend.exception.UnauthorizedException;
import com.photography.backend.service.CloudinaryService;
import com.photography.backend.service.GalleryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final CloudinaryService cloudinaryService;
    private final GalleryService galleryService;
    private final String expectedApiKey;

    public AdminController(
            CloudinaryService cloudinaryService,
            GalleryService galleryService,
            @Value("${admin.api-key}") String expectedApiKey
    ) {
        this.cloudinaryService = cloudinaryService;
        this.galleryService = galleryService;
        this.expectedApiKey = expectedApiKey;
    }

    @PostConstruct
    public void validateApiKey() {
        if (expectedApiKey == null || expectedApiKey.isBlank()) {
            throw new IllegalStateException("ADMIN_API_KEY environment variable is required but not set. Application cannot start without admin API key.");
        }
    }

    private void checkApiKey(String apiKey) {
        if (apiKey == null || !apiKey.equals(expectedApiKey)) {
            throw new UnauthorizedException("Invalid or missing X-API-KEY header");
        }
    }

    @GetMapping("/images")
    public ApiResponseDTO<List<ImageDTO>> getAllImages(@RequestHeader(value = "X-API-KEY", required = false) String apiKey) {
        checkApiKey(apiKey);
        List<ImageDTO> images = galleryService.getAllImagesForAdmin();
        return ApiResponseDTO.ok("All images retrieved for admin management", images);
    }

    @PostMapping("/images")
    public ResponseEntity<ApiResponseDTO<ImageDTO>> uploadImage(
            @RequestHeader(value = "X-API-KEY", required = false) String apiKey,
            @RequestParam("file") MultipartFile file,
            @RequestParam("category") String category,
            @RequestParam(value = "altText", required = false, defaultValue = "") String altText,
            @RequestParam(value = "displayOrder", required = false, defaultValue = "0") Integer displayOrder,
            @RequestParam(value = "heroOrder", required = false, defaultValue = "0") Integer heroOrder,
            @RequestParam(value = "selectedWorksOrder", required = false, defaultValue = "0") Integer selectedWorksOrder,
            @RequestParam(value = "isFeatured", required = false, defaultValue = "false") Boolean isFeatured,
            @RequestParam(value = "showInHero", required = false, defaultValue = "false") Boolean showInHero,
            @RequestParam(value = "showInSelectedWorks", required = false, defaultValue = "false") Boolean showInSelectedWorks
    ) {
        checkApiKey(apiKey);

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.error("File cannot be empty"));
        }

        try {
            CloudinaryService.UploadResult result = cloudinaryService.uploadImage(file, category);
            Image newImage = new Image(
                    result.getUrl(),
                    result.getPublicId(),
                    category,
                    altText,
                    displayOrder,
                    isFeatured,
                    showInHero,
                    showInSelectedWorks,
                    heroOrder,
                    selectedWorksOrder
            );

            ImageDTO savedImage = galleryService.saveImage(newImage);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.ok("Image uploaded successfully to Cloudinary & DB", savedImage));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Failed to upload image: " + e.getMessage()));
        }
    }

    @PatchMapping("/images/{id}")
    public ApiResponseDTO<ImageDTO> updateImage(
            @RequestHeader(value = "X-API-KEY", required = false) String apiKey,
            @PathVariable Long id,
            @RequestBody ImageUpdateDTO dto
    ) {
        checkApiKey(apiKey);
        ImageDTO updated = galleryService.updateImage(id, dto);
        return ApiResponseDTO.ok("Image placement & metadata updated successfully", updated);
    }

    @DeleteMapping("/images/{id}")
    public ApiResponseDTO<Void> deleteImage(
            @RequestHeader(value = "X-API-KEY", required = false) String apiKey,
            @PathVariable Long id
    ) {
        checkApiKey(apiKey);
        galleryService.deleteImage(id);
        return ApiResponseDTO.ok("Image deleted successfully", null);
    }
}

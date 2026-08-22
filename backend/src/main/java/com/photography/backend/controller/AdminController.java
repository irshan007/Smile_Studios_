package com.photography.backend.controller;

import com.photography.backend.dto.ApiResponseDTO;
import com.photography.backend.dto.ImageDTO;
import com.photography.backend.dto.ImageUpdateDTO;
import com.photography.backend.entity.Image;
import com.photography.backend.exception.ResourceNotFoundException;
import com.photography.backend.exception.UnauthorizedException;
import com.photography.backend.service.GalleryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final GalleryService galleryService;

    @Value("${admin.api-key:default_secret_key_change_me}")
    private String adminApiKey;

    public AdminController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    private void validateAdminKey(HttpServletRequest request) {
        String providedKey = request.getHeader("ADMIN_API_KEY");
        if (providedKey == null || !providedKey.equals(adminApiKey)) {
            throw new UnauthorizedException("Invalid or missing ADMIN_API_KEY header");
        }
    }

    @GetMapping("/images")
    public ResponseEntity<ApiResponseDTO<List<ImageDTO>>> getAllImages(HttpServletRequest request) {
        validateAdminKey(request);
        List<ImageDTO> images = galleryService.getAllImagesForAdmin();
        return ResponseEntity.ok(ApiResponseDTO.ok("All images retrieved successfully", images));
    }

    @PostMapping("/images")
    public ResponseEntity<ApiResponseDTO<ImageDTO>> uploadImage(
            HttpServletRequest request,
            @RequestParam("file") MultipartFile file,
            @RequestParam("category") String category,
            @RequestParam(value = "altText", required = false) String altText,
            @RequestParam(value = "displayOrder", required = false) Integer displayOrder,
            @RequestParam(value = "isFeatured", required = false) Boolean isFeatured,
            @RequestParam(value = "showInHero", required = false) Boolean showInHero,
            @RequestParam(value = "heroOrder", required = false) Integer heroOrder,
            @RequestParam(value = "showInSelectedWorks", required = false) Boolean showInSelectedWorks,
            @RequestParam(value = "selectedWorksOrder", required = false) Integer selectedWorksOrder
    ) throws IOException {
        validateAdminKey(request);

        String generatedId = "img_" + UUID.randomUUID().toString().substring(0, 8);
        String mockUrl = "https://images.unsplash.com/photo-1519741497674-611481863552?auto=format&fit=crop&q=80&w=1600";

        Image image = new Image(
                mockUrl,
                generatedId,
                category,
                altText != null ? altText : file.getOriginalFilename(),
                displayOrder != null ? displayOrder : 0,
                isFeatured != null ? isFeatured : false,
                showInHero != null ? showInHero : false,
                showInSelectedWorks != null ? showInSelectedWorks : false,
                heroOrder != null ? heroOrder : 0,
                selectedWorksOrder != null ? selectedWorksOrder : 0
        );

        ImageDTO saved = galleryService.saveImage(image);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Image uploaded successfully (in-memory)", saved));
    }

    @PatchMapping("/images/{id}")
    public ResponseEntity<ApiResponseDTO<ImageDTO>> updateImage(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestBody ImageUpdateDTO dto
    ) {
        validateAdminKey(request);
        ImageDTO updated = galleryService.updateImage(id, dto);
        return ResponseEntity.ok(ApiResponseDTO.ok("Image updated successfully", updated));
    }

    @DeleteMapping("/images/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteImage(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        validateAdminKey(request);

        galleryService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + id));

        galleryService.deleteImage(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Image deleted successfully", null));
    }
}
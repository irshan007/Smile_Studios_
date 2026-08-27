package com.photography.backend.service;

import com.photography.backend.dto.CloudinaryUploadResult;
import com.photography.backend.dto.CreatePortfolioWorkRequest;
import com.photography.backend.dto.PortfolioWorkResponseDTO;
import com.photography.backend.dto.UpdatePortfolioWorkRequest;
import com.photography.backend.entity.Category;
import com.photography.backend.entity.PortfolioWork;
import com.photography.backend.entity.WorkImage;
import com.photography.backend.exception.ResourceNotFoundException;
import com.photography.backend.repository.CategoryRepository;
import com.photography.backend.repository.PortfolioWorkRepository;
import com.photography.backend.repository.WorkImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PortfolioWorkService {

    private static final Logger logger = LoggerFactory.getLogger(PortfolioWorkService.class);

    private final PortfolioWorkRepository portfolioWorkRepository;
    private final WorkImageRepository workImageRepository;
    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;

    public PortfolioWorkService(
            PortfolioWorkRepository portfolioWorkRepository,
            WorkImageRepository workImageRepository,
            CategoryRepository categoryRepository,
            CloudinaryService cloudinaryService) {
        this.portfolioWorkRepository = portfolioWorkRepository;
        this.workImageRepository = workImageRepository;
        this.categoryRepository = categoryRepository;
        this.cloudinaryService = cloudinaryService;
    }

    /**
     * Create a new portfolio work with cover image and optional gallery images.
     */
    @Transactional
    public PortfolioWorkResponseDTO createWork(
            CreatePortfolioWorkRequest request,
            MultipartFile coverImage,
            List<MultipartFile> galleryImages
    ) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("Portfolio work title is required.");
        }
        if (coverImage == null || coverImage.isEmpty()) {
            throw new IllegalArgumentException("Cover image is required when creating a portfolio work.");
        }

        Category category = resolveCategory(request.getCategoryId(), request.getCategorySlug(), request.getCategoryName());

        String slug = generateUniqueSlug(request.getTitle(), null);

        PortfolioWork work = new PortfolioWork();
        work.setTitle(request.getTitle().trim());
        work.setSlug(slug);
        work.setDescription(request.getDescription());
        work.setCategory(category);
        work.setIsPublished(request.getIsPublished() != null ? request.getIsPublished() : false);
        work.setIsFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false);
        work.setShowInHero(request.getShowInHero() != null ? request.getShowInHero() : false);
        work.setShowInSelectedWorks(request.getShowInSelectedWorks() != null ? request.getShowInSelectedWorks() : false);
        work.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);

        // Upload Cover Image
        CloudinaryUploadResult coverResult = cloudinaryService.uploadImage(coverImage);
        work.setCoverImageUrl(coverResult.getSecureUrl());
        work.setCoverImagePublicId(coverResult.getPublicId());

        // Upload Gallery Images if present
        if (galleryImages != null && !galleryImages.isEmpty()) {
            int order = 1;
            for (MultipartFile file : galleryImages) {
                if (file != null && !file.isEmpty()) {
                    CloudinaryUploadResult imgResult = cloudinaryService.uploadImage(file);
                    WorkImage workImage = new WorkImage(
                            imgResult.getSecureUrl(),
                            imgResult.getPublicId(),
                            request.getTitle() + " - Image " + order,
                            order
                    );
                    work.addImage(workImage);
                    order++;
                }
            }
        }

        PortfolioWork savedWork = portfolioWorkRepository.save(work);
        logger.info("Successfully created portfolio work with ID: {}, slug: '{}'", savedWork.getId(), savedWork.getSlug());
        return new PortfolioWorkResponseDTO(savedWork);
    }

    /**
     * Get all portfolio works (published and draft) for admin.
     */
    @Transactional(readOnly = true)
    public List<PortfolioWorkResponseDTO> getAllWorksForAdmin() {
        List<PortfolioWork> works = portfolioWorkRepository.findAll(
                Sort.by(Sort.Direction.ASC, "displayOrder").and(Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        return works.stream().map(PortfolioWorkResponseDTO::new).collect(Collectors.toList());
    }

    /**
     * Get a single portfolio work by ID for admin.
     */
    @Transactional(readOnly = true)
    public PortfolioWorkResponseDTO getWorkById(Long id) {
        PortfolioWork work = portfolioWorkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio work not found with ID: " + id));
        return new PortfolioWorkResponseDTO(work);
    }

    /**
     * Get published works for public view, supporting optional filtering by category and featured status.
     */
    @Transactional(readOnly = true)
    public List<PortfolioWorkResponseDTO> getPublishedWorks(String categorySlug, Boolean featured) {
        List<PortfolioWork> works;

        if (categorySlug != null && !categorySlug.isBlank()) {
            Category category = categoryRepository.findBySlug(categorySlug.toLowerCase().trim())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with slug: " + categorySlug));

            if (Boolean.TRUE.equals(featured)) {
                works = portfolioWorkRepository.findByCategoryIdAndIsPublishedTrueOrderByDisplayOrderAsc(category.getId())
                        .stream().filter(w -> Boolean.TRUE.equals(w.getIsFeatured()))
                        .collect(Collectors.toList());
            } else {
                works = portfolioWorkRepository.findByCategoryIdAndIsPublishedTrueOrderByDisplayOrderAsc(category.getId());
            }
        } else if (Boolean.TRUE.equals(featured)) {
            works = portfolioWorkRepository.findByIsFeaturedTrueAndIsPublishedTrueOrderByDisplayOrderAsc();
        } else {
            works = portfolioWorkRepository.findByIsPublishedTrueOrderByDisplayOrderAsc();
        }

        return works.stream().map(PortfolioWorkResponseDTO::new).collect(Collectors.toList());
    }

    /**
     * Get a single published portfolio work by slug for public view.
     */
    @Transactional(readOnly = true)
    public PortfolioWorkResponseDTO getPublishedWorkBySlug(String slug) {
        PortfolioWork work = portfolioWorkRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio work not found with slug: " + slug));

        if (!Boolean.TRUE.equals(work.getIsPublished())) {
            throw new ResourceNotFoundException("Portfolio work not found with slug: " + slug);
        }

        return new PortfolioWorkResponseDTO(work);
    }

    /**
     * Update an existing portfolio work.
     */
    @Transactional
    public PortfolioWorkResponseDTO updateWork(
            Long id,
            UpdatePortfolioWorkRequest request,
            MultipartFile coverImage,
            List<MultipartFile> newGalleryImages
    ) {
        PortfolioWork work = portfolioWorkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio work not found with ID: " + id));

        // Update Title and Slug if changed
        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            if (!request.getTitle().trim().equalsIgnoreCase(work.getTitle())) {
                work.setTitle(request.getTitle().trim());
                work.setSlug(generateUniqueSlug(request.getTitle(), work.getId()));
            }
        }

        if (request.getDescription() != null) {
            work.setDescription(request.getDescription());
        }

        // Update Category if specified
        if (request.getCategoryId() != null || request.getCategorySlug() != null || request.getCategoryName() != null) {
            Category category = resolveCategory(request.getCategoryId(), request.getCategorySlug(), request.getCategoryName());
            work.setCategory(category);
        }

        if (request.getIsPublished() != null) {
            work.setIsPublished(request.getIsPublished());
        }
        if (request.getIsFeatured() != null) {
            work.setIsFeatured(request.getIsFeatured());
        }
        if (request.getShowInHero() != null) {
            work.setShowInHero(request.getShowInHero());
        }
        if (request.getShowInSelectedWorks() != null) {
            work.setShowInSelectedWorks(request.getShowInSelectedWorks());
        }
        if (request.getDisplayOrder() != null) {
            work.setDisplayOrder(request.getDisplayOrder());
        }

        // Handle Cover Image Update
        if (coverImage != null && !coverImage.isEmpty()) {
            String oldPublicId = work.getCoverImagePublicId();
            CloudinaryUploadResult coverResult = cloudinaryService.uploadImage(coverImage);
            work.setCoverImageUrl(coverResult.getSecureUrl());
            work.setCoverImagePublicId(coverResult.getPublicId());

            if (oldPublicId != null && !oldPublicId.isBlank()) {
                try {
                    cloudinaryService.deleteImage(oldPublicId);
                } catch (Exception e) {
                    logger.warn("Failed to delete previous cover image '{}' from Cloudinary: {}", oldPublicId, e.getMessage());
                }
            }
        }

        // Handle New Gallery Images
        if (newGalleryImages != null && !newGalleryImages.isEmpty()) {
            int currentOrder = work.getImages().size() + 1;
            for (MultipartFile file : newGalleryImages) {
                if (file != null && !file.isEmpty()) {
                    CloudinaryUploadResult imgResult = cloudinaryService.uploadImage(file);
                    WorkImage workImage = new WorkImage(
                            imgResult.getSecureUrl(),
                            imgResult.getPublicId(),
                            work.getTitle() + " - Image " + currentOrder,
                            currentOrder
                    );
                    work.addImage(workImage);
                    currentOrder++;
                }
            }
        }

        PortfolioWork updatedWork = portfolioWorkRepository.save(work);
        logger.info("Successfully updated portfolio work with ID: {}", updatedWork.getId());
        return new PortfolioWorkResponseDTO(updatedWork);
    }

    /**
     * Set published status (publish/unpublish) for a portfolio work.
     */
    @Transactional
    public PortfolioWorkResponseDTO setPublishedStatus(Long id, boolean published) {
        PortfolioWork work = portfolioWorkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio work not found with ID: " + id));

        work.setIsPublished(published);
        PortfolioWork updatedWork = portfolioWorkRepository.save(work);
        logger.info("Portfolio work ID {} published status set to {}", id, published);
        return new PortfolioWorkResponseDTO(updatedWork);
    }

    /**
     * Delete a portfolio work and its associated Cloudinary images (cover & gallery images).
     */
    @Transactional
    public void deleteWork(Long id) {
        PortfolioWork work = portfolioWorkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio work not found with ID: " + id));

        // Delete cover image from Cloudinary
        if (work.getCoverImagePublicId() != null && !work.getCoverImagePublicId().isBlank()) {
            try {
                cloudinaryService.deleteImage(work.getCoverImagePublicId());
            } catch (Exception e) {
                logger.warn("Could not delete cover image '{}' from Cloudinary: {}", work.getCoverImagePublicId(), e.getMessage());
            }
        }

        // Delete all gallery images from Cloudinary
        for (WorkImage img : new ArrayList<>(work.getImages())) {
            if (img.getCloudinaryPublicId() != null && !img.getCloudinaryPublicId().isBlank()) {
                try {
                    cloudinaryService.deleteImage(img.getCloudinaryPublicId());
                } catch (Exception e) {
                    logger.warn("Could not delete gallery image '{}' from Cloudinary: {}", img.getCloudinaryPublicId(), e.getMessage());
                }
            }
        }

        portfolioWorkRepository.delete(work);
        logger.info("Successfully deleted portfolio work ID: {} and its Cloudinary assets", id);
    }

    /**
     * Delete an individual gallery image from a portfolio work.
     */
    @Transactional
    public void deleteGalleryImage(Long workId, Long imageId) {
        WorkImage workImage = workImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery image not found with ID: " + imageId));

        if (workImage.getPortfolioWork() == null || !workImage.getPortfolioWork().getId().equals(workId)) {
            throw new IllegalArgumentException("Gallery image ID " + imageId + " does not belong to portfolio work ID " + workId);
        }

        // Delete from Cloudinary
        if (workImage.getCloudinaryPublicId() != null && !workImage.getCloudinaryPublicId().isBlank()) {
            try {
                cloudinaryService.deleteImage(workImage.getCloudinaryPublicId());
            } catch (Exception e) {
                logger.warn("Could not delete gallery image '{}' from Cloudinary: {}", workImage.getCloudinaryPublicId(), e.getMessage());
            }
        }

        PortfolioWork parentWork = workImage.getPortfolioWork();
        parentWork.removeImage(workImage);
        workImageRepository.delete(workImage);
        logger.info("Successfully deleted gallery image ID: {} from portfolio work ID: {}", imageId, workId);
    }

    /**
     * Generates a unique, URL-safe slug from a title string.
     */
    public String generateUniqueSlug(String title, Long currentWorkId) {
        if (title == null || title.isBlank()) {
            title = "untitled-work";
        }

        String baseSlug = title.toLowerCase(Locale.ENGLISH)
                .replaceAll("[^a-z0-9\\s-]", "")
                .trim()
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");

        if (baseSlug.isBlank()) {
            baseSlug = "work";
        }

        String slug = baseSlug;
        int counter = 1;

        while (true) {
            Optional<PortfolioWork> existing = portfolioWorkRepository.findBySlug(slug);
            if (existing.isEmpty() || (currentWorkId != null && existing.get().getId().equals(currentWorkId))) {
                return slug;
            }
            slug = baseSlug + "-" + counter;
            counter++;
        }
    }

    /**
     * Resolves an existing Category by categoryId, categorySlug, or categoryName.
     */
    private Category resolveCategory(Long categoryId, String categorySlug, String categoryName) {
        if (categoryId != null) {
            return categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));
        }

        String identifier = (categorySlug != null && !categorySlug.isBlank()) ? categorySlug : categoryName;
        if (identifier != null && !identifier.isBlank()) {
            String cleanSlug = identifier.toLowerCase(Locale.ENGLISH).trim().replaceAll("[^a-z0-9]", "-");
            Optional<Category> bySlug = categoryRepository.findBySlug(cleanSlug);
            if (bySlug.isPresent()) {
                return bySlug.get();
            }
            // Try direct slug match
            return categoryRepository.findBySlug(identifier.toLowerCase().trim())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with name or slug: " + identifier));
        }

        throw new IllegalArgumentException("Category is required. Please specify categoryId, categorySlug, or categoryName.");
    }
}

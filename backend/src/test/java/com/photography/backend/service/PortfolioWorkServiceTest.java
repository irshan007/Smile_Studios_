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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PortfolioWorkServiceTest {

    @Mock
    private PortfolioWorkRepository portfolioWorkRepository;

    @Mock
    private WorkImageRepository workImageRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private PortfolioWorkService portfolioWorkService;

    private Category weddingCategory;
    private PortfolioWork sampleWork;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        weddingCategory = new Category("Weddings", "weddings", 1);
        weddingCategory.setId(10L);

        sampleWork = new PortfolioWork("Arun & Priya Wedding", "arun-priya-wedding", "Sample wedding description", weddingCategory);
        sampleWork.setId(100L);
        sampleWork.setCoverImageUrl("https://res.cloudinary.com/demo/image/upload/cover.jpg");
        sampleWork.setCoverImagePublicId("smile-studios/portfolio/cover_123");
        sampleWork.setIsPublished(true);
        sampleWork.setIsFeatured(true);
    }

    @Test
    @DisplayName("Create valid portfolio work with cover and gallery images")
    void testCreateValidPortfolioWork() {
        CreatePortfolioWorkRequest request = new CreatePortfolioWorkRequest();
        request.setTitle("Priya & Rahul Wedding");
        request.setDescription("A beautiful Tamil wedding");
        request.setCategoryId(10L);
        request.setIsPublished(true);

        MockMultipartFile coverFile = new MockMultipartFile("coverImage", "cover.jpg", "image/jpeg", "cover-bytes".getBytes());
        MockMultipartFile galleryFile1 = new MockMultipartFile("galleryImages", "g1.jpg", "image/jpeg", "g1-bytes".getBytes());

        when(categoryRepository.findById(10L)).thenReturn(Optional.of(weddingCategory));
        when(portfolioWorkRepository.findBySlug("priya-rahul-wedding")).thenReturn(Optional.empty());

        when(cloudinaryService.uploadImage(coverFile)).thenReturn(
                new CloudinaryUploadResult("smile-studios/portfolio/cover_1", "https://cloudinary.com/cover1.jpg", "http://cloudinary.com/cover1.jpg", "jpg", 800, 600, 50000L)
        );
        when(cloudinaryService.uploadImage(galleryFile1)).thenReturn(
                new CloudinaryUploadResult("smile-studios/portfolio/g1", "https://cloudinary.com/g1.jpg", "http://cloudinary.com/g1.jpg", "jpg", 800, 600, 40000L)
        );

        when(portfolioWorkRepository.save(any(PortfolioWork.class))).thenAnswer(invocation -> {
            PortfolioWork saved = invocation.getArgument(0);
            saved.setId(101L);
            return saved;
        });

        PortfolioWorkResponseDTO result = portfolioWorkService.createWork(request, coverFile, List.of(galleryFile1));

        assertNotNull(result);
        assertEquals(101L, result.getId());
        assertEquals("Priya & Rahul Wedding", result.getTitle());
        assertEquals("priya-rahul-wedding", result.getSlug());
        assertEquals("https://cloudinary.com/cover1.jpg", result.getCoverImageUrl());
        assertEquals(1, result.getImages().size());
        assertEquals("https://cloudinary.com/g1.jpg", result.getImages().get(0).getImageUrl());

        verify(cloudinaryService, times(2)).uploadImage(any(MockMultipartFile.class));
        verify(portfolioWorkRepository, times(1)).save(any(PortfolioWork.class));
    }

    @Test
    @DisplayName("Create work with missing title should throw IllegalArgumentException")
    void testCreateWorkMissingTitle() {
        CreatePortfolioWorkRequest request = new CreatePortfolioWorkRequest();
        request.setTitle("   ");

        MockMultipartFile coverFile = new MockMultipartFile("coverImage", "cover.jpg", "image/jpeg", "cover-bytes".getBytes());

        assertThrows(IllegalArgumentException.class, () ->
                portfolioWorkService.createWork(request, coverFile, null)
        );
    }

    @Test
    @DisplayName("Create work with invalid category should throw ResourceNotFoundException")
    void testCreateWorkInvalidCategory() {
        CreatePortfolioWorkRequest request = new CreatePortfolioWorkRequest();
        request.setTitle("Sample Work");
        request.setCategoryId(999L);

        MockMultipartFile coverFile = new MockMultipartFile("coverImage", "cover.jpg", "image/jpeg", "cover-bytes".getBytes());

        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                portfolioWorkService.createWork(request, coverFile, null)
        );
    }

    @Test
    @DisplayName("Admin can retrieve both draft and published works")
    void testAdminGetWorks() {
        PortfolioWork draftWork = new PortfolioWork("Draft Work", "draft-work", "Draft desc", weddingCategory);
        draftWork.setId(102L);
        draftWork.setIsPublished(false);

        when(portfolioWorkRepository.findAll(any(org.springframework.data.domain.Sort.class)))
                .thenReturn(List.of(sampleWork, draftWork));

        List<PortfolioWorkResponseDTO> results = portfolioWorkService.getAllWorksForAdmin();

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(w -> !w.getIsPublished()));
        assertTrue(results.stream().anyMatch(PortfolioWorkResponseDTO::getIsPublished));
    }

    @Test
    @DisplayName("Public API returns only published works")
    void testPublicGetWorksReturnsOnlyPublished() {
        when(portfolioWorkRepository.findByIsPublishedTrueOrderByDisplayOrderAsc()).thenReturn(List.of(sampleWork));

        List<PortfolioWorkResponseDTO> results = portfolioWorkService.getPublishedWorks(null, false);

        assertEquals(1, results.size());
        assertTrue(results.get(0).getIsPublished());
    }

    @Test
    @DisplayName("Unpublished work is not exposed publicly by slug")
    void testGetUnpublishedWorkBySlugFails() {
        PortfolioWork draftWork = new PortfolioWork("Draft Work", "draft-slug", "Draft desc", weddingCategory);
        draftWork.setId(103L);
        draftWork.setIsPublished(false);

        when(portfolioWorkRepository.findBySlug("draft-slug")).thenReturn(Optional.of(draftWork));

        assertThrows(ResourceNotFoundException.class, () ->
                portfolioWorkService.getPublishedWorkBySlug("draft-slug")
        );
    }

    @Test
    @DisplayName("Update work metadata, category, and images")
    void testUpdateWork() {
        UpdatePortfolioWorkRequest request = new UpdatePortfolioWorkRequest();
        request.setTitle("Arun & Priya Grand Wedding");
        request.setIsFeatured(false);

        Category newCategory = new Category("Events", "events", 2);
        newCategory.setId(20L);
        request.setCategoryId(20L);

        MockMultipartFile newCover = new MockMultipartFile("coverImage", "new_cover.jpg", "image/jpeg", "new-cover".getBytes());

        when(portfolioWorkRepository.findById(100L)).thenReturn(Optional.of(sampleWork));
        when(categoryRepository.findById(20L)).thenReturn(Optional.of(newCategory));
        when(portfolioWorkRepository.findBySlug("arun-priya-grand-wedding")).thenReturn(Optional.empty());

        when(cloudinaryService.uploadImage(newCover)).thenReturn(
                new CloudinaryUploadResult("smile-studios/portfolio/new_cover_123", "https://cloudinary.com/new_cover.jpg", "http://cloudinary.com/new_cover.jpg", "jpg", 800, 600, 60000L)
        );
        when(portfolioWorkRepository.save(any(PortfolioWork.class))).thenAnswer(i -> i.getArgument(0));

        PortfolioWorkResponseDTO updated = portfolioWorkService.updateWork(100L, request, newCover, null);

        assertNotNull(updated);
        assertEquals("Arun & Priya Grand Wedding", updated.getTitle());
        assertEquals("arun-priya-grand-wedding", updated.getSlug());
        assertEquals("Events", updated.getCategory());
        assertEquals("https://cloudinary.com/new_cover.jpg", updated.getCoverImageUrl());

        // Old cover image should be deleted from Cloudinary
        verify(cloudinaryService, times(1)).deleteImage("smile-studios/portfolio/cover_123");
    }

    @Test
    @DisplayName("Publish and unpublish portfolio work")
    void testPublishAndUnpublish() {
        when(portfolioWorkRepository.findById(100L)).thenReturn(Optional.of(sampleWork));
        when(portfolioWorkRepository.save(any(PortfolioWork.class))).thenAnswer(i -> i.getArgument(0));

        PortfolioWorkResponseDTO unpublished = portfolioWorkService.setPublishedStatus(100L, false);
        assertFalse(unpublished.getIsPublished());

        PortfolioWorkResponseDTO published = portfolioWorkService.setPublishedStatus(100L, true);
        assertTrue(published.getIsPublished());
    }

    @Test
    @DisplayName("Delete work deletes cover image, gallery images, and database record")
    void testDeleteWork() {
        WorkImage img1 = new WorkImage("https://cloudinary.com/img1.jpg", "smile-studios/portfolio/img1", "Alt 1", 1);
        img1.setId(501L);
        sampleWork.addImage(img1);

        when(portfolioWorkRepository.findById(100L)).thenReturn(Optional.of(sampleWork));

        portfolioWorkService.deleteWork(100L);

        verify(cloudinaryService, times(1)).deleteImage("smile-studios/portfolio/cover_123");
        verify(cloudinaryService, times(1)).deleteImage("smile-studios/portfolio/img1");
        verify(portfolioWorkRepository, times(1)).delete(sampleWork);
    }

    @Test
    @DisplayName("Delete individual gallery image verifies work association and cleans up DB & Cloudinary")
    void testDeleteGalleryImage() {
        WorkImage img1 = new WorkImage("https://cloudinary.com/img1.jpg", "smile-studios/portfolio/img1", "Alt 1", 1);
        img1.setId(501L);
        sampleWork.addImage(img1);

        when(workImageRepository.findById(501L)).thenReturn(Optional.of(img1));

        portfolioWorkService.deleteGalleryImage(100L, 501L);

        verify(cloudinaryService, times(1)).deleteImage("smile-studios/portfolio/img1");
        verify(workImageRepository, times(1)).delete(img1);
        assertFalse(sampleWork.getImages().contains(img1));
    }
}

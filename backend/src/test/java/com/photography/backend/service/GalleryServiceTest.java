package com.photography.backend.service;

import com.photography.backend.dto.ImageDTO;
import com.photography.backend.entity.Category;
import com.photography.backend.entity.PortfolioWork;
import com.photography.backend.repository.CategoryRepository;
import com.photography.backend.repository.PortfolioWorkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for GalleryService hero and selected-works methods.
 *
 * Step 10B: Verifies that the home gallery endpoints delegate to
 * PortfolioWorkRepository (PostgreSQL) and return the ImageDTO shape
 * that Home.jsx expects.
 */
class GalleryServiceTest {

    @Mock
    private PortfolioWorkRepository portfolioWorkRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private GalleryService galleryService;

    private Category weddings;
    private Category engagement;
    private Category portraits;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Use the @Autowired constructor so portfolioWorkRepository is injected
        galleryService = new GalleryService(categoryRepository, portfolioWorkRepository);

        weddings = new Category("Weddings", "weddings", 1);
        weddings.setId(1L);

        engagement = new Category("Engagements", "engagement", 2);
        engagement.setId(2L);

        portraits = new Category("Portraits", "portraits", 3);
        portraits.setId(3L);
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private PortfolioWork makeWork(Long id, String title, String coverUrl, Category category,
                                    boolean published, boolean showInHero, boolean showInSelectedWorks,
                                    int displayOrder) {
        PortfolioWork work = new PortfolioWork(title, title.toLowerCase().replace(" ", "-"), "Desc", category);
        work.setId(id);
        work.setCoverImageUrl(coverUrl);
        work.setCoverImagePublicId("smile-studios/portfolio/pub_id_" + id);
        work.setIsPublished(published);
        work.setIsFeatured(false);
        work.setShowInHero(showInHero);
        work.setShowInSelectedWorks(showInSelectedWorks);
        work.setDisplayOrder(displayOrder);
        return work;
    }

    // -----------------------------------------------------------------------
    // HERO ENDPOINT TESTS
    // -----------------------------------------------------------------------

    @Nested
    @DisplayName("GET /api/gallery/hero — PostgreSQL delegation")
    class HeroImageTests {

        @Test
        @DisplayName("Returns published works where showInHero=true, mapped to ImageDTO")
        void testHeroReturnsPublishedShowInHeroWorks() {
            PortfolioWork w1 = makeWork(1L, "Classic Wedding Stories",
                    "https://res.cloudinary.com/demo/image/upload/wedding_cover.jpg",
                    weddings, true, true, false, 1);
            PortfolioWork w2 = makeWork(2L, "Quiet Luxury Sessions",
                    "https://res.cloudinary.com/demo/image/upload/engagement_cover.jpg",
                    engagement, true, true, false, 2);

            when(portfolioWorkRepository.findByShowInHeroTrueAndIsPublishedTrueOrderByDisplayOrderAsc())
                    .thenReturn(List.of(w1, w2));

            List<ImageDTO> result = galleryService.getHeroImages();

            assertEquals(2, result.size());

            // Verify first item mapping: coverImageUrl → url, title → altText, category.name → category
            ImageDTO dto1 = result.get(0);
            assertEquals(1L, dto1.getId());
            assertEquals("https://res.cloudinary.com/demo/image/upload/wedding_cover.jpg", dto1.getUrl());
            assertEquals("Classic Wedding Stories", dto1.getAltText());
            assertEquals("Weddings", dto1.getCategory());
            assertEquals(1, dto1.getDisplayOrder());
            assertTrue(dto1.getShowInHero());

            ImageDTO dto2 = result.get(1);
            assertEquals(2L, dto2.getId());
            assertEquals("https://res.cloudinary.com/demo/image/upload/engagement_cover.jpg", dto2.getUrl());
            assertEquals("Quiet Luxury Sessions", dto2.getAltText());
            assertEquals("Engagements", dto2.getCategory());

            verify(portfolioWorkRepository, times(1))
                    .findByShowInHeroTrueAndIsPublishedTrueOrderByDisplayOrderAsc();
        }

        @Test
        @DisplayName("Returns empty list when no works have showInHero=true (DB returns empty)")
        void testHeroReturnsEmptyWhenNoFlaggedWorks() {
            when(portfolioWorkRepository.findByShowInHeroTrueAndIsPublishedTrueOrderByDisplayOrderAsc())
                    .thenReturn(List.of());

            List<ImageDTO> result = galleryService.getHeroImages();

            assertNotNull(result);
            assertTrue(result.isEmpty(), "Should return empty list, not null or in-memory fallback");

            verify(portfolioWorkRepository, times(1))
                    .findByShowInHeroTrueAndIsPublishedTrueOrderByDisplayOrderAsc();
        }

        @Test
        @DisplayName("Excludes unpublished works (repository contract enforces isPublished=true)")
        void testHeroExcludesUnpublishedWorks() {
            // Repository query already filters isPublished=true — simulate it returning only published
            PortfolioWork published = makeWork(3L, "Shoreline Romance",
                    "https://res.cloudinary.com/demo/image/upload/pre_wedding.jpg",
                    weddings, true, true, false, 1);

            when(portfolioWorkRepository.findByShowInHeroTrueAndIsPublishedTrueOrderByDisplayOrderAsc())
                    .thenReturn(List.of(published)); // unpublished work NOT in result by repository contract

            List<ImageDTO> result = galleryService.getHeroImages();

            assertEquals(1, result.size());
            assertEquals("Shoreline Romance", result.get(0).getAltText());
            // Confirm the correct (published-only) query is called
            verify(portfolioWorkRepository, times(1))
                    .findByShowInHeroTrueAndIsPublishedTrueOrderByDisplayOrderAsc();
            // The unpublished query variant is NEVER called
            verify(portfolioWorkRepository, never())
                    .findByIsPublishedTrueOrderByDisplayOrderAsc();
        }

        @Test
        @DisplayName("Excludes works where showInHero=false (repository contract enforces showInHero=true)")
        void testHeroExcludesShowInHeroFalseWorks() {
            // Simulate repository already filtering: only returns showInHero=true works
            PortfolioWork heroWork = makeWork(4L, "Fine Art Portraits",
                    "https://res.cloudinary.com/demo/image/upload/portrait.jpg",
                    portraits, true, true, false, 1);

            when(portfolioWorkRepository.findByShowInHeroTrueAndIsPublishedTrueOrderByDisplayOrderAsc())
                    .thenReturn(List.of(heroWork));

            List<ImageDTO> result = galleryService.getHeroImages();

            assertEquals(1, result.size());
            // Confirm showInHero is mapped correctly into ImageDTO
            assertTrue(result.get(0).getShowInHero(), "Mapped ImageDTO.showInHero should be true");
        }

        @Test
        @DisplayName("Results preserve displayOrder ascending from repository (order is pre-sorted)")
        void testHeroOrderByDisplayOrder() {
            PortfolioWork w1 = makeWork(1L, "First", "https://cdn.example.com/a.jpg", weddings, true, true, false, 1);
            PortfolioWork w2 = makeWork(2L, "Second", "https://cdn.example.com/b.jpg", engagement, true, true, false, 2);
            PortfolioWork w3 = makeWork(3L, "Third", "https://cdn.example.com/c.jpg", portraits, true, true, false, 3);

            when(portfolioWorkRepository.findByShowInHeroTrueAndIsPublishedTrueOrderByDisplayOrderAsc())
                    .thenReturn(List.of(w1, w2, w3));

            List<ImageDTO> result = galleryService.getHeroImages();

            assertEquals(3, result.size());
            assertEquals(1, result.get(0).getDisplayOrder());
            assertEquals(2, result.get(1).getDisplayOrder());
            assertEquals(3, result.get(2).getDisplayOrder());
        }

        @Test
        @DisplayName("Hero ImageDTO has url, altText and category populated (Home.jsx compatibility)")
        void testHeroDtoFieldsCompatibleWithHomePage() {
            PortfolioWork w = makeWork(10L, "Classic Wedding Stories",
                    "https://res.cloudinary.com/demo/image/upload/cover.jpg",
                    weddings, true, true, false, 1);

            when(portfolioWorkRepository.findByShowInHeroTrueAndIsPublishedTrueOrderByDisplayOrderAsc())
                    .thenReturn(List.of(w));

            List<ImageDTO> result = galleryService.getHeroImages();
            ImageDTO dto = result.get(0);

            // Home.jsx reads: img.url, img.altText, img.category
            assertNotNull(dto.getUrl(), "url must not be null (used as img src)");
            assertNotNull(dto.getAltText(), "altText must not be null (used as slide title)");
            assertNotNull(dto.getCategory(), "category must not be null (used for slide tag)");
        }
    }

    // -----------------------------------------------------------------------
    // SELECTED WORKS ENDPOINT TESTS
    // -----------------------------------------------------------------------

    @Nested
    @DisplayName("GET /api/gallery/selected-works — PostgreSQL delegation")
    class SelectedWorksImageTests {

        @Test
        @DisplayName("Returns published works where showInSelectedWorks=true, mapped to ImageDTO")
        void testSelectedWorksReturnsPublishedFlaggedWorks() {
            PortfolioWork w1 = makeWork(1L, "Classic Wedding Stories",
                    "https://res.cloudinary.com/demo/image/upload/wedding.jpg",
                    weddings, true, false, true, 1);
            PortfolioWork w2 = makeWork(2L, "Quiet Luxury Sessions",
                    "https://res.cloudinary.com/demo/image/upload/engagement.jpg",
                    engagement, true, false, true, 2);

            when(portfolioWorkRepository.findByShowInSelectedWorksTrueAndIsPublishedTrueOrderByDisplayOrderAsc())
                    .thenReturn(List.of(w1, w2));

            List<ImageDTO> result = galleryService.getSelectedWorksImages();

            assertEquals(2, result.size());

            ImageDTO dto1 = result.get(0);
            assertEquals(1L, dto1.getId());
            assertEquals("https://res.cloudinary.com/demo/image/upload/wedding.jpg", dto1.getUrl());
            assertEquals("Classic Wedding Stories", dto1.getAltText());
            assertEquals("Weddings", dto1.getCategory());
            assertTrue(dto1.getShowInSelectedWorks());

            ImageDTO dto2 = result.get(1);
            assertEquals(2L, dto2.getId());
            assertEquals("Quiet Luxury Sessions", dto2.getAltText());
            assertEquals("Engagements", dto2.getCategory());

            verify(portfolioWorkRepository, times(1))
                    .findByShowInSelectedWorksTrueAndIsPublishedTrueOrderByDisplayOrderAsc();
        }

        @Test
        @DisplayName("Returns empty list when no works have showInSelectedWorks=true")
        void testSelectedWorksReturnsEmptyWhenNoFlaggedWorks() {
            when(portfolioWorkRepository.findByShowInSelectedWorksTrueAndIsPublishedTrueOrderByDisplayOrderAsc())
                    .thenReturn(List.of());

            List<ImageDTO> result = galleryService.getSelectedWorksImages();

            assertNotNull(result);
            assertTrue(result.isEmpty(), "Should return empty list, not fallback to in-memory");

            verify(portfolioWorkRepository, times(1))
                    .findByShowInSelectedWorksTrueAndIsPublishedTrueOrderByDisplayOrderAsc();
        }

        @Test
        @DisplayName("Excludes unpublished works (repository contract enforces isPublished=true)")
        void testSelectedWorksExcludesUnpublishedWorks() {
            PortfolioWork published = makeWork(5L, "Milestones & New Beginnings",
                    "https://res.cloudinary.com/demo/image/upload/maternity.jpg",
                    weddings, true, false, true, 1);

            when(portfolioWorkRepository.findByShowInSelectedWorksTrueAndIsPublishedTrueOrderByDisplayOrderAsc())
                    .thenReturn(List.of(published));

            List<ImageDTO> result = galleryService.getSelectedWorksImages();

            assertEquals(1, result.size());
            verify(portfolioWorkRepository, times(1))
                    .findByShowInSelectedWorksTrueAndIsPublishedTrueOrderByDisplayOrderAsc();
            verify(portfolioWorkRepository, never())
                    .findByIsPublishedTrueOrderByDisplayOrderAsc();
        }

        @Test
        @DisplayName("Excludes works where showInSelectedWorks=false (repository contract)")
        void testSelectedWorksExcludesShowInSelectedWorksFalseWorks() {
            PortfolioWork flagged = makeWork(6L, "Reception Storytelling",
                    "https://res.cloudinary.com/demo/image/upload/reception.jpg",
                    weddings, true, false, true, 1);

            when(portfolioWorkRepository.findByShowInSelectedWorksTrueAndIsPublishedTrueOrderByDisplayOrderAsc())
                    .thenReturn(List.of(flagged));

            List<ImageDTO> result = galleryService.getSelectedWorksImages();

            assertEquals(1, result.size());
            assertTrue(result.get(0).getShowInSelectedWorks(),
                    "Mapped ImageDTO.showInSelectedWorks should be true");
        }

        @Test
        @DisplayName("Results preserve displayOrder ascending from repository")
        void testSelectedWorksOrderByDisplayOrder() {
            PortfolioWork w1 = makeWork(1L, "A", "https://cdn.example.com/a.jpg", weddings, true, false, true, 1);
            PortfolioWork w2 = makeWork(2L, "B", "https://cdn.example.com/b.jpg", engagement, true, false, true, 2);
            PortfolioWork w3 = makeWork(3L, "C", "https://cdn.example.com/c.jpg", portraits, true, false, true, 3);
            PortfolioWork w4 = makeWork(4L, "D", "https://cdn.example.com/d.jpg", weddings, true, false, true, 4);
            PortfolioWork w5 = makeWork(5L, "E", "https://cdn.example.com/e.jpg", engagement, true, false, true, 5);
            PortfolioWork w6 = makeWork(6L, "F", "https://cdn.example.com/f.jpg", portraits, true, false, true, 6);

            when(portfolioWorkRepository.findByShowInSelectedWorksTrueAndIsPublishedTrueOrderByDisplayOrderAsc())
                    .thenReturn(List.of(w1, w2, w3, w4, w5, w6));

            List<ImageDTO> result = galleryService.getSelectedWorksImages();

            assertEquals(6, result.size());
            for (int i = 0; i < 6; i++) {
                assertEquals(i + 1, result.get(i).getDisplayOrder(),
                        "Item at index " + i + " should have displayOrder " + (i + 1));
            }
        }

        @Test
        @DisplayName("Selected works ImageDTO has url, altText and category (Home.jsx compatibility)")
        void testSelectedWorksDtoFieldsCompatibleWithHomePage() {
            PortfolioWork w = makeWork(20L, "Shoreline Romance",
                    "https://res.cloudinary.com/demo/image/upload/prewedding.jpg",
                    engagement, true, false, true, 1);

            when(portfolioWorkRepository.findByShowInSelectedWorksTrueAndIsPublishedTrueOrderByDisplayOrderAsc())
                    .thenReturn(List.of(w));

            List<ImageDTO> result = galleryService.getSelectedWorksImages();
            ImageDTO dto = result.get(0);

            // Home.jsx reads: img.url, img.altText || img.category (for card title/alt)
            assertNotNull(dto.getUrl(), "url must not be null (used as CloudinaryImage src)");
            assertNotNull(dto.getAltText(), "altText must not be null (used as work-card title)");
            assertNotNull(dto.getCategory(), "category must not be null (used as work-card label)");
        }
    }

    // -----------------------------------------------------------------------
    // MAPPING CORRECTNESS TESTS
    // -----------------------------------------------------------------------

    @Nested
    @DisplayName("portfolioWorkToImageDTO mapping")
    class MappingTests {

        @Test
        @DisplayName("All relevant PortfolioWork fields are correctly mapped to ImageDTO")
        void testFullMapping() {
            PortfolioWork w = makeWork(99L, "Test Work Title",
                    "https://res.cloudinary.com/demo/image/upload/test.jpg",
                    weddings, true, true, true, 5);
            w.setCoverImagePublicId("smile-studios/portfolio/weddings/test_pub_id");
            w.setIsFeatured(true);

            when(portfolioWorkRepository.findByShowInHeroTrueAndIsPublishedTrueOrderByDisplayOrderAsc())
                    .thenReturn(List.of(w));

            List<ImageDTO> result = galleryService.getHeroImages();
            ImageDTO dto = result.get(0);

            assertEquals(99L, dto.getId());
            assertEquals("https://res.cloudinary.com/demo/image/upload/test.jpg", dto.getUrl());
            assertEquals("smile-studios/portfolio/weddings/test_pub_id", dto.getCloudinaryPublicId());
            assertEquals("Test Work Title", dto.getAltText());
            assertEquals("Weddings", dto.getCategory());
            assertEquals(5, dto.getDisplayOrder());
            assertEquals(5, dto.getHeroOrder(),    "heroOrder should mirror displayOrder");
            assertEquals(5, dto.getSelectedWorksOrder(), "selectedWorksOrder should mirror displayOrder");
            assertTrue(dto.getIsFeatured());
            assertTrue(dto.getShowInHero());
            assertTrue(dto.getShowInSelectedWorks());
        }

        @Test
        @DisplayName("Works with null category are handled gracefully (category field is null in DTO)")
        void testNullCategoryHandled() {
            PortfolioWork w = new PortfolioWork("No Category Work", "no-cat", "Desc", null);
            w.setId(50L);
            w.setCoverImageUrl("https://cdn.example.com/img.jpg");
            w.setIsPublished(true);
            w.setShowInHero(true);
            w.setDisplayOrder(1);

            when(portfolioWorkRepository.findByShowInHeroTrueAndIsPublishedTrueOrderByDisplayOrderAsc())
                    .thenReturn(List.of(w));

            List<ImageDTO> result = galleryService.getHeroImages();

            assertEquals(1, result.size());
            assertNull(result.get(0).getCategory(), "category should be null when PortfolioWork has no category");
            assertNotNull(result.get(0).getUrl());
        }
    }

    // -----------------------------------------------------------------------
    // IN-MEMORY FALLBACK TESTS (non-Spring instantiation)
    // -----------------------------------------------------------------------

    @Nested
    @DisplayName("In-memory fallback — no-arg constructor (non-Spring context)")
    class InMemoryFallbackTests {

        @Test
        @DisplayName("No-arg constructor uses in-memory seed for hero (portfolioWorkRepository is null)")
        void testNoArgConstructorHeroFallback() {
            GalleryService noRepoService = new GalleryService();

            List<ImageDTO> result = noRepoService.getHeroImages();

            // Seed has 4 items with showInHero=true
            assertFalse(result.isEmpty(), "In-memory fallback should return seeded hero images");
            result.forEach(dto -> assertTrue(dto.getShowInHero(),
                    "All returned in-memory images should have showInHero=true"));
        }

        @Test
        @DisplayName("No-arg constructor uses in-memory seed for selected-works (portfolioWorkRepository is null)")
        void testNoArgConstructorSelectedWorksFallback() {
            GalleryService noRepoService = new GalleryService();

            List<ImageDTO> result = noRepoService.getSelectedWorksImages();

            assertFalse(result.isEmpty(), "In-memory fallback should return seeded selected-works images");
            result.forEach(dto -> assertTrue(dto.getShowInSelectedWorks(),
                    "All returned in-memory images should have showInSelectedWorks=true"));
        }
    }
}

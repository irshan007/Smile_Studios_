package com.photography.backend.service;

import com.photography.backend.dto.CategorySummaryDTO;
import com.photography.backend.dto.ImageDTO;
import com.photography.backend.dto.ImageUpdateDTO;
import com.photography.backend.entity.Category;
import com.photography.backend.entity.Image;
import com.photography.backend.entity.PortfolioWork;
import com.photography.backend.entity.WorkImage;
import com.photography.backend.exception.ResourceNotFoundException;
import com.photography.backend.repository.CategoryRepository;
import com.photography.backend.repository.PortfolioWorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class GalleryService {

    private final List<Image> images = new CopyOnWriteArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    private final CategoryRepository categoryRepository;
    private final PortfolioWorkRepository portfolioWorkRepository;

    public static final List<String> ALL_CATEGORIES = Arrays.asList(
            "Weddings",
            "Pre Weddings",
            "Engagement",
            "Portraits",
            "Events",
            "Maternity & Baby"
    );

    public GalleryService() {
        this.categoryRepository = null;
        this.portfolioWorkRepository = null;
        seedImages();
    }

    @Autowired
    public GalleryService(CategoryRepository categoryRepository, PortfolioWorkRepository portfolioWorkRepository) {
        this.categoryRepository = categoryRepository;
        this.portfolioWorkRepository = portfolioWorkRepository;
        seedImages();
    }

    public GalleryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        this.portfolioWorkRepository = null;
        seedImages();
    }

    private void seedImages() {
        List<Image> sampleImages = Arrays.asList(
                // Hero Slideshow & Featured Portfolios (showInHero = true)
                new Image(idCounter.getAndIncrement(), "https://images.unsplash.com/photo-1519741497674-611481863552?auto=format&fit=crop&q=80&w=1800", "hero_1", "Weddings", "Graceful Luxury Wedding Portrait", 1, true, true, true, 1, 1),
                new Image(idCounter.getAndIncrement(), "https://images.unsplash.com/photo-1511285560929-80b456fea0bc?auto=format&fit=crop&q=80&w=1800", "hero_2", "Weddings", "Luxury Sunset Couple Portrait", 2, true, true, true, 2, 2),
                new Image(idCounter.getAndIncrement(), "https://images.unsplash.com/photo-1583939003579-730e3918a45a?auto=format&fit=crop&q=80&w=1800", "hero_3", "Portraits", "Intimate Studio Moment", 3, true, true, true, 3, 3),
                new Image(idCounter.getAndIncrement(), "https://images.unsplash.com/photo-1606800052052-a08af7148866?auto=format&fit=crop&q=80&w=1800", "hero_4", "Portraits", "Editorial Fashion Portraiture", 4, true, true, true, 4, 4),

                // Pre Weddings
                new Image(idCounter.getAndIncrement(), "https://images.unsplash.com/photo-1520854221256-17451cc331bf?auto=format&fit=crop&q=80&w=1800", "pw_1", "Pre Weddings", "Beachside Sunset Pre Wedding", 1, true, false, true, 0, 5),
                new Image(idCounter.getAndIncrement(), "https://images.unsplash.com/photo-1532712938310-34cb3982ef74?auto=format&fit=crop&q=80&w=1800", "pw_2", "Pre Weddings", "Heritage Palace Romance", 2, true, false, true, 0, 6),
                new Image(idCounter.getAndIncrement(), "https://images.unsplash.com/photo-1465495976277-4387d4b0b4c6?auto=format&fit=crop&q=80&w=1800", "pw_3", "Pre Weddings", "Mountain View Couple", 3, false, false, false, 0, 0),

                // Weddings
                new Image(idCounter.getAndIncrement(), "https://images.unsplash.com/photo-1610030469983-98e550d6193c?auto=format&fit=crop&q=80&w=1800", "w_1", "Weddings", "Traditional Muhurtham Ritual", 1, true, false, true, 0, 7),
                new Image(idCounter.getAndIncrement(), "https://images.unsplash.com/photo-1544078751-58fee2d8a03b?auto=format&fit=crop&q=80&w=1800", "w_2", "Weddings", "Silk Sari Bride Elegance", 2, true, false, false, 0, 0),
                new Image(idCounter.getAndIncrement(), "https://images.unsplash.com/photo-1545232979-fbfd42e2006f?auto=format&fit=crop&q=80&w=1800", "w_3", "Weddings", "Traditional Ceremony Moment", 3, true, false, true, 0, 8),

                // Engagement
                new Image(idCounter.getAndIncrement(), "https://images.unsplash.com/photo-1465495976277-4387d4b0b4c6?auto=format&fit=crop&q=80&w=1800", "eng_1", "Engagement", "Ring Ceremony Toast", 1, true, false, false, 0, 0),

                // Events
                new Image(idCounter.getAndIncrement(), "https://images.unsplash.com/photo-1511795409834-ef04bbd61622?auto=format&fit=crop&q=80&w=1800", "evt_1", "Events", "Gala Stage Lighting", 1, true, false, true, 0, 9),
                new Image(idCounter.getAndIncrement(), "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?auto=format&fit=crop&q=80&w=1800", "evt_2", "Events", "Celebration Atmosphere", 2, false, false, false, 0, 0),

                // Maternity & Baby
                new Image(idCounter.getAndIncrement(), "https://images.unsplash.com/photo-1555252333-9f8e92e65df9?auto=format&fit=crop&q=80&w=1800", "mat_1", "Maternity & Baby", "Maternity Glow Portrait", 1, true, false, true, 0, 10),
                new Image(idCounter.getAndIncrement(), "https://images.unsplash.com/photo-1519689680058-324335c77eba?auto=format&fit=crop&q=80&w=1800", "mat_2", "Maternity & Baby", "Newborn Soft Moments", 2, false, false, false, 0, 0)
        );

        images.addAll(sampleImages);
    }

    @Transactional(readOnly = true)
    public List<CategorySummaryDTO> getCategories() {
        if (categoryRepository != null && portfolioWorkRepository != null) {
            List<Category> dbCategories = categoryRepository.findAllByOrderByDisplayOrderAsc();
            if (!dbCategories.isEmpty()) {
                List<CategorySummaryDTO> summaries = new ArrayList<>();
                for (Category cat : dbCategories) {
                    List<PortfolioWork> publishedWorks = portfolioWorkRepository
                            .findByCategoryIdAndIsPublishedTrueOrderByDisplayOrderAsc(cat.getId());

                    String coverUrl = findCategoryCoverUrl(publishedWorks);
                    int imageCount = publishedWorks.stream()
                            .mapToInt(work -> work.getImages() != null ? work.getImages().size() : 0)
                            .sum();

                    summaries.add(new CategorySummaryDTO(cat, coverUrl, imageCount));
                }
                return summaries;
            }
        }

        // Fallback for non-spring test instantiation
        List<CategorySummaryDTO> summaries = new ArrayList<>();
        int order = 1;
        for (String catName : ALL_CATEGORIES) {
            String slug = catName.toLowerCase(Locale.ENGLISH).replaceAll("[^a-z0-9]+", "-");
            List<Image> categoryImages = images.stream()
                    .filter(img -> catName.equalsIgnoreCase(img.getCategory()))
                    .sorted(Comparator.comparingInt(img -> img.getDisplayOrder() != null ? img.getDisplayOrder() : 0))
                    .toList();

            String coverUrl = !categoryImages.isEmpty()
                    ? categoryImages.get(0).getCloudinaryUrl()
                    : "https://images.unsplash.com/photo-1519741497674-611481863552?auto=format&fit=crop&q=80&w=1200";

            summaries.add(new CategorySummaryDTO((long) order, catName, slug, order, coverUrl, categoryImages.size()));
            order++;
        }

        return summaries;
    }

    private String findCategoryCoverUrl(List<PortfolioWork> publishedWorks) {
        for (PortfolioWork work : publishedWorks) {
            if (work.getCoverImageUrl() != null && !work.getCoverImageUrl().isBlank()) {
                return work.getCoverImageUrl();
            }
        }

        return publishedWorks.stream()
                .filter(work -> work.getImages() != null)
                .flatMap(work -> work.getImages().stream())
                .filter(image -> image.getImageUrl() != null && !image.getImageUrl().isBlank())
                .sorted(Comparator.comparingInt(this::safeImageDisplayOrder))
                .map(WorkImage::getImageUrl)
                .findFirst()
                .orElse(null);
    }

    private int safeImageDisplayOrder(WorkImage image) {
        return image.getDisplayOrder() != null ? image.getDisplayOrder() : 0;
    }

    public List<ImageDTO> getImagesByCategory(String categoryParam) {
        String targetCategory = ALL_CATEGORIES.stream()
                .filter(cat -> cat.equalsIgnoreCase(categoryParam) || cat.toLowerCase(Locale.ENGLISH).replaceAll("[^a-z0-9]+", "-").equalsIgnoreCase(categoryParam))
                .findFirst()
                .orElse(categoryParam);

        return images.stream()
                .filter(img -> targetCategory.equalsIgnoreCase(img.getCategory()))
                .sorted(Comparator.comparingInt(img -> img.getDisplayOrder() != null ? img.getDisplayOrder() : 0))
                .map(ImageDTO::new)
                .collect(Collectors.toList());
    }

    public List<ImageDTO> getHeroImages() {
        if (portfolioWorkRepository != null) {
            List<PortfolioWork> works = portfolioWorkRepository
                    .findByShowInHeroTrueAndIsPublishedTrueOrderByDisplayOrderAsc();
            return works.stream()
                    .map(GalleryService::portfolioWorkToImageDTO)
                    .collect(Collectors.toList());
        }
        // Non-Spring fallback (in-memory seed)
        return images.stream()
                .filter(img -> Boolean.TRUE.equals(img.getShowInHero()))
                .sorted(Comparator.comparingInt(img -> img.getHeroOrder() != null ? img.getHeroOrder() : 0))
                .map(ImageDTO::new)
                .collect(Collectors.toList());
    }

    public List<ImageDTO> getSelectedWorksImages() {
        if (portfolioWorkRepository != null) {
            List<PortfolioWork> works = portfolioWorkRepository
                    .findByShowInSelectedWorksTrueAndIsPublishedTrueOrderByDisplayOrderAsc();
            return works.stream()
                    .map(GalleryService::portfolioWorkToImageDTO)
                    .collect(Collectors.toList());
        }
        // Non-Spring fallback (in-memory seed)
        return images.stream()
                .filter(img -> Boolean.TRUE.equals(img.getShowInSelectedWorks()))
                .sorted(Comparator.comparingInt(img -> img.getSelectedWorksOrder() != null ? img.getSelectedWorksOrder() : 0))
                .map(ImageDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Maps a PortfolioWork record to the ImageDTO shape expected by the Home page.
     * <p>
     * Mapping rules (matches what Home.jsx reads from the API response):
     * <ul>
     *   <li>coverImageUrl  → url       (hero-slide-bg src / CloudinaryImage src)</li>
     *   <li>title          → altText   (slide title / work-card alt text)</li>
     *   <li>category.name  → category  (slide tag prefix / work-card label)</li>
     *   <li>id             → id        (stable React key)</li>
     *   <li>displayOrder   → displayOrder (sort field, already applied upstream)</li>
     *   <li>isFeatured / showInHero / showInSelectedWorks passed through for completeness</li>
     * </ul>
     */
    private static ImageDTO portfolioWorkToImageDTO(PortfolioWork work) {
        ImageDTO dto = new ImageDTO();
        dto.setId(work.getId());
        dto.setUrl(work.getCoverImageUrl());
        dto.setCloudinaryPublicId(work.getCoverImagePublicId());
        dto.setAltText(work.getTitle());
        dto.setCategory(work.getCategory() != null ? work.getCategory().getName() : null);
        dto.setDisplayOrder(work.getDisplayOrder());
        dto.setIsFeatured(work.getIsFeatured());
        dto.setShowInHero(work.getShowInHero());
        dto.setShowInSelectedWorks(work.getShowInSelectedWorks());
        // heroOrder / selectedWorksOrder come from displayOrder for PortfolioWork records
        dto.setHeroOrder(work.getDisplayOrder());
        dto.setSelectedWorksOrder(work.getDisplayOrder());
        return dto;
    }

    public List<ImageDTO> getAllImagesForAdmin() {
        return images.stream()
                .sorted(Comparator.comparingInt(img -> img.getDisplayOrder() != null ? img.getDisplayOrder() : 0))
                .map(ImageDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<Image> findById(Long id) {
        return images.stream().filter(img -> img.getId().equals(id)).findFirst();
    }

    public ImageDTO saveImage(Image image) {
        if (image.getId() == null) {
            image.setId(idCounter.getAndIncrement());
        }
        images.add(image);
        return new ImageDTO(image);
    }

    public ImageDTO updateImage(Long id, ImageUpdateDTO dto) {
        Image image = findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + id));

        if (dto.getCategory() != null) image.setCategory(dto.getCategory());
        if (dto.getAltText() != null) image.setAltText(dto.getAltText());
        if (dto.getDisplayOrder() != null) image.setDisplayOrder(dto.getDisplayOrder());
        if (dto.getHeroOrder() != null) image.setHeroOrder(dto.getHeroOrder());
        if (dto.getSelectedWorksOrder() != null) image.setSelectedWorksOrder(dto.getSelectedWorksOrder());
        if (dto.getIsFeatured() != null) image.setIsFeatured(dto.getIsFeatured());
        if (dto.getShowInHero() != null) image.setShowInHero(dto.getShowInHero());
        if (dto.getShowInSelectedWorks() != null) image.setShowInSelectedWorks(dto.getShowInSelectedWorks());

        return new ImageDTO(image);
    }

    public void deleteImage(Long id) {
        boolean removed = images.removeIf(img -> img.getId().equals(id));
        if (!removed) {
            throw new ResourceNotFoundException("Image not found with id: " + id);
        }
    }
}

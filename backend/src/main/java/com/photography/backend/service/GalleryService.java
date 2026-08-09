package com.photography.backend.service;

import com.photography.backend.dto.CategorySummaryDTO;
import com.photography.backend.dto.ImageDTO;
import com.photography.backend.dto.ImageUpdateDTO;
import com.photography.backend.entity.Image;
import com.photography.backend.exception.ResourceNotFoundException;
import com.photography.backend.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GalleryService {

    private final ImageRepository imageRepository;

    public static final List<String> ALL_CATEGORIES = Arrays.asList(
            "Portraits",
            "Pre Weddings",
            "Tamil Weddings",
            "Telugu Weddings",
            "Brahmin Weddings",
            "Christian Weddings",
            "Muslim Weddings",
            "Engagement",
            "Events",
            "Maternity/Baby"
    );

    public GalleryService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Transactional(readOnly = true)
    public List<CategorySummaryDTO> getCategories() {
        List<CategorySummaryDTO> summaries = new ArrayList<>();

        for (String catName : ALL_CATEGORIES) {
            String slug = catName.toLowerCase().replaceAll("[^a-z0-9]+", "-");
            List<Image> images = imageRepository.findByCategoryOrderByDisplayOrderAsc(catName);
            String coverUrl = !images.isEmpty()
                    ? images.get(0).getCloudinaryUrl()
                    : "https://images.unsplash.com/photo-1519741497674-611481863552?auto=format&fit=crop&q=80&w=1200";

            summaries.add(new CategorySummaryDTO(catName, slug, coverUrl, images.size()));
        }

        return summaries;
    }

    @Transactional(readOnly = true)
    public List<ImageDTO> getImagesByCategory(String categoryParam) {
        String targetCategory = ALL_CATEGORIES.stream()
                .filter(cat -> cat.equalsIgnoreCase(categoryParam) || cat.toLowerCase().replaceAll("[^a-z0-9]+", "-").equalsIgnoreCase(categoryParam))
                .findFirst()
                .orElse(categoryParam);

        return imageRepository.findByCategoryOrderByDisplayOrderAsc(targetCategory)
                .stream()
                .map(ImageDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ImageDTO> getHeroImages() {
        return imageRepository.findByShowInHeroTrueOrderByHeroOrderAsc()
                .stream()
                .map(ImageDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ImageDTO> getSelectedWorksImages() {
        return imageRepository.findByShowInSelectedWorksTrueOrderBySelectedWorksOrderAsc()
                .stream()
                .map(ImageDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ImageDTO> getAllImagesForAdmin() {
        return imageRepository.findAllByOrderByDisplayOrderAsc()
                .stream()
                .map(ImageDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<Image> findById(Long id) {
        return imageRepository.findById(id);
    }

    @Transactional
    public ImageDTO saveImage(Image image) {
        Image saved = imageRepository.save(image);
        return new ImageDTO(saved);
    }

    @Transactional
    public ImageDTO updateImage(Long id, ImageUpdateDTO dto) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + id));

        if (dto.getCategory() != null) image.setCategory(dto.getCategory());
        if (dto.getAltText() != null) image.setAltText(dto.getAltText());
        if (dto.getDisplayOrder() != null) image.setDisplayOrder(dto.getDisplayOrder());
        if (dto.getHeroOrder() != null) image.setHeroOrder(dto.getHeroOrder());
        if (dto.getSelectedWorksOrder() != null) image.setSelectedWorksOrder(dto.getSelectedWorksOrder());
        if (dto.getIsFeatured() != null) image.setIsFeatured(dto.getIsFeatured());
        if (dto.getShowInHero() != null) image.setShowInHero(dto.getShowInHero());
        if (dto.getShowInSelectedWorks() != null) image.setShowInSelectedWorks(dto.getShowInSelectedWorks());

        Image updated = imageRepository.save(image);
        return new ImageDTO(updated);
    }

    @Transactional
    public void deleteImage(Long id) {
        if (!imageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Image not found with id: " + id);
        }
        imageRepository.deleteById(id);
    }
}

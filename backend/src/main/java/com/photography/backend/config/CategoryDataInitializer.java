package com.photography.backend.config;

import com.photography.backend.entity.Category;
import com.photography.backend.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Order(1)
public class CategoryDataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CategoryDataInitializer.class);

    private final CategoryRepository categoryRepository;

    public CategoryDataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private static class CategorySpec {
        final String name;
        final String slug;
        final int displayOrder;

        CategorySpec(String name, String slug, int displayOrder) {
            this.name = name;
            this.slug = slug;
            this.displayOrder = displayOrder;
        }
    }

    private static final List<CategorySpec> TARGET_CATEGORIES = Arrays.asList(
            new CategorySpec("Weddings", "weddings", 1),
            new CategorySpec("Pre Weddings", "pre-weddings", 2),
            new CategorySpec("Engagement", "engagement", 3),
            new CategorySpec("Portraits", "portraits", 4),
            new CategorySpec("Events", "events", 5),
            new CategorySpec("Maternity & Baby", "maternity-baby", 6)
    );

    private static final List<String> DEPRECATED_SLUGS = Arrays.asList(
            "tamil-weddings",
            "telugu-weddings",
            "brahmin-weddings",
            "christian-weddings",
            "muslim-weddings"
    );

    @Override
    public void run(String... args) {
        logger.info("Synchronizing backend portfolio categories...");

        for (CategorySpec spec : TARGET_CATEGORIES) {
            Optional<Category> existing = categoryRepository.findBySlug(spec.slug);
            if (existing.isPresent()) {
                Category cat = existing.get();
                boolean updated = false;
                if (!spec.name.equals(cat.getName())) {
                    cat.setName(spec.name);
                    updated = true;
                }
                if (cat.getDisplayOrder() == null || cat.getDisplayOrder() != spec.displayOrder) {
                    cat.setDisplayOrder(spec.displayOrder);
                    updated = true;
                }
                if (updated) {
                    categoryRepository.save(cat);
                    logger.info("Updated existing category '{}' ({})", spec.name, spec.slug);
                }
            } else {
                Category newCat = new Category(spec.name, spec.slug, spec.displayOrder);
                categoryRepository.save(newCat);
                logger.info("Created missing category '{}' ({})", spec.name, spec.slug);
            }
        }

        // Clean up deprecated subcategories if they exist and have no associated works
        for (String deprecatedSlug : DEPRECATED_SLUGS) {
            categoryRepository.findBySlug(deprecatedSlug).ifPresent(cat -> {
                if (cat.getWorks() == null || cat.getWorks().isEmpty()) {
                    categoryRepository.delete(cat);
                    logger.info("Removed legacy category '{}' ({})", cat.getName(), cat.getSlug());
                } else {
                    logger.warn("Legacy category '{}' ({}) has associated works; keeping to avoid data loss.", cat.getName(), cat.getSlug());
                }
            });
        }

        logger.info("Backend portfolio categories synchronization complete.");
    }
}

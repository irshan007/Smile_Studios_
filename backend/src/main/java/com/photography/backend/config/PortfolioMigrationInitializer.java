package com.photography.backend.config;

import com.photography.backend.dto.CloudinaryUploadResult;
import com.photography.backend.entity.Category;
import com.photography.backend.entity.PortfolioWork;
import com.photography.backend.entity.WorkImage;
import com.photography.backend.repository.CategoryRepository;
import com.photography.backend.repository.PortfolioWorkRepository;
import com.photography.backend.service.CloudinaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

@Component
@Profile("!test")
@Order(2)
public class PortfolioMigrationInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(PortfolioMigrationInitializer.class);

    private final PortfolioWorkRepository portfolioWorkRepository;
    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;

    @Value("${portfolio.migration.enabled:false}")
    private boolean migrationEnabled;

    public PortfolioMigrationInitializer(
            PortfolioWorkRepository portfolioWorkRepository,
            CategoryRepository categoryRepository,
            CloudinaryService cloudinaryService) {
        this.portfolioWorkRepository = portfolioWorkRepository;
        this.categoryRepository = categoryRepository;
        this.cloudinaryService = cloudinaryService;
    }

    private static class MigrationWorkSpec {
        final String title;
        final String slug;
        final String categorySlug;
        final int displayOrder;
        final String coverPath;
        final List<String> galleryPaths;

        MigrationWorkSpec(String title, String slug, String categorySlug, int displayOrder, String coverPath, List<String> galleryPaths) {
            this.title = title;
            this.slug = slug;
            this.categorySlug = categorySlug;
            this.displayOrder = displayOrder;
            this.coverPath = coverPath;
            this.galleryPaths = galleryPaths;
        }
    }

    private static final List<MigrationWorkSpec> MIGRATION_SPECS = Arrays.asList(
            new MigrationWorkSpec(
                    "Classic Wedding Stories",
                    "classic-wedding-stories",
                    "weddings",
                    1,
                    "frontend/src/assets/Project_Source/wedding/c1.png",
                    Arrays.asList(
                            "frontend/src/assets/Project_Source/wedding/c1.png",
                            "frontend/src/assets/Project_Source/wedding/c2.png",
                            "frontend/src/assets/Project_Source/wedding/c3.png",
                            "frontend/src/assets/Project_Source/wedding/c4.png",
                            "frontend/src/assets/Project_Source/wedding/c5.png",
                            "frontend/src/assets/Project_Source/reception/c1 (1).png"
                    )
            ),
            new MigrationWorkSpec(
                    "Shoreline Romance",
                    "shoreline-romance",
                    "pre-weddings",
                    2,
                    "frontend/src/assets/Project_Source/pre wedding/c1.png",
                    Arrays.asList(
                            "frontend/src/assets/Project_Source/pre wedding/c1.png",
                            "frontend/src/assets/Project_Source/pre wedding/c2.png",
                            "frontend/src/assets/Project_Source/pre wedding/c3.png",
                            "frontend/src/assets/Project_Source/pre wedding/c4.png",
                            "frontend/src/assets/Project_Source/pre wedding/c5 (1).png"
                    )
            ),
            new MigrationWorkSpec(
                    "Quiet Luxury Sessions",
                    "quiet-luxury-sessions",
                    "engagement",
                    3,
                    "frontend/src/assets/Project_Source/engagement photos/c2.png",
                    Arrays.asList(
                            "frontend/src/assets/Project_Source/engagement photos/c1.png",
                            "frontend/src/assets/Project_Source/engagement photos/c2.png",
                            "frontend/src/assets/Project_Source/engagement photos/c4.png",
                            "frontend/src/assets/Project_Source/engagement photos/c5.png",
                            "frontend/src/assets/Project_Source/engagement photos/engagement photos.png"
                    )
            ),
            new MigrationWorkSpec(
                    "Fine Art & Editorial Portraits",
                    "fine-art-editorial-portraits",
                    "portraits",
                    4,
                    "frontend/src/assets/Project_Source/puberty/c1.png",
                    Arrays.asList(
                            "frontend/src/assets/Project_Source/puberty/c1.png",
                            "frontend/src/assets/Project_Source/puberty/c2.png",
                            "frontend/src/assets/Project_Source/puberty/c3.png",
                            "frontend/src/assets/Project_Source/puberty/c4.png",
                            "frontend/src/assets/Project_Source/puberty/c5 (1).png"
                    )
            ),
            new MigrationWorkSpec(
                    "Reception Storytelling & Celebrations",
                    "reception-storytelling-celebrations",
                    "events",
                    5,
                    "frontend/src/assets/Project_Source/reception/c2.png",
                    Arrays.asList(
                            "frontend/src/assets/Project_Source/reception/c1 (1).png",
                            "frontend/src/assets/Project_Source/reception/c2.png",
                            "frontend/src/assets/Project_Source/reception/c3.png",
                            "frontend/src/assets/Project_Source/reception/c4.png",
                            "frontend/src/assets/Project_Source/reception/c5.png",
                            "frontend/src/assets/Project_Source/wedding/c5.png"
                    )
            ),
            new MigrationWorkSpec(
                    "Milestones & New Beginnings",
                    "milestones-new-beginnings",
                    "maternity-baby",
                    6,
                    "frontend/src/assets/Project_Source/maternity/c2.png",
                    Arrays.asList(
                            "frontend/src/assets/Project_Source/maternity/c1.png",
                            "frontend/src/assets/Project_Source/maternity/c2.png",
                            "frontend/src/assets/Project_Source/maternity/c3.png",
                            "frontend/src/assets/Project_Source/maternity/c4.png",
                            "frontend/src/assets/Project_Source/maternity/c5 (1).png",
                            "frontend/src/assets/Project_Source/baby shoot/c1 (1).png",
                            "frontend/src/assets/Project_Source/baby shoot/c2.png",
                            "frontend/src/assets/Project_Source/baby shoot/c3.png"
                    )
            )
    );

    @Override
    public void run(String... args) {
        if (!migrationEnabled) {
            logger.info("Portfolio migration is disabled (portfolio.migration.enabled=false). Skipping startup migration.");
            return;
        }

        String activeProfiles = System.getProperty("spring.profiles.active", "");
        if (activeProfiles.contains("test")) {
            logger.info("Test profile active. Skipping portfolio migration.");
            return;
        }

        if (!cloudinaryService.isConfigured()) {
            logger.info("Cloudinary credentials are not configured. Skipping static portfolio migration.");
            return;
        }

        logger.info("Portfolio migration started.");

        List<MigrationWorkSpec> pendingSpecs = new ArrayList<>();
        for (MigrationWorkSpec spec : MIGRATION_SPECS) {
            if (!portfolioWorkRepository.existsBySlug(spec.slug)) {
                pendingSpecs.add(spec);
            } else {
                logger.info("Skipping already migrated work: {}", spec.title);
            }
        }

        if (pendingSpecs.isEmpty()) {
            logger.info("Portfolio migration completed successfully.");
            return;
        }

        // Step 1: Pre-verify all required source image files exist before uploading anything
        List<String> missingFiles = new ArrayList<>();
        for (MigrationWorkSpec spec : pendingSpecs) {
            File coverFile = resolveSourceFile(spec.coverPath);
            if (!coverFile.exists()) {
                missingFiles.add(spec.coverPath + " (resolved: " + coverFile.getAbsolutePath() + ")");
            }
            for (String gPath : spec.galleryPaths) {
                File gFile = resolveSourceFile(gPath);
                if (!gFile.exists()) {
                    missingFiles.add(gPath + " (resolved: " + gFile.getAbsolutePath() + ")");
                }
            }
        }

        if (!missingFiles.isEmpty()) {
            logger.error("Portfolio migration aborted! The following required source image files do not exist: {}", missingFiles);
            return;
        }

        // Cache Cloudinary upload results by source file path to avoid re-uploading shared images
        Map<String, CloudinaryUploadResult> uploadCache = new HashMap<>();

        // Step 2: Perform migration for each pending work
        for (MigrationWorkSpec spec : pendingSpecs) {
            try {
                logger.info("Migrating work: {}", spec.title);

                Optional<Category> catOpt = categoryRepository.findBySlug(spec.categorySlug);
                if (catOpt.isEmpty()) {
                    logger.error("Category '{}' not found in database. Aborting migration for work '{}'.", spec.categorySlug, spec.title);
                    continue;
                }
                Category category = catOpt.get();

                // Upload Cover Image
                CloudinaryUploadResult coverResult = uploadFileCached(spec.coverPath, spec.categorySlug, uploadCache, 1, spec.galleryPaths.size());

                PortfolioWork work = new PortfolioWork();
                work.setTitle(spec.title);
                work.setSlug(spec.slug);
                work.setCategory(category);
                work.setDescription("Curated " + category.getName() + " portfolio collection showcasing timeless photography.");
                work.setCoverImageUrl(coverResult.getSecureUrl());
                work.setCoverImagePublicId(coverResult.getPublicId());
                work.setIsPublished(true);
                work.setIsFeatured(false);
                work.setShowInHero(false);
                work.setShowInSelectedWorks(false);
                work.setDisplayOrder(spec.displayOrder);

                // Process Gallery Images
                int order = 1;
                for (String gPath : spec.galleryPaths) {
                    CloudinaryUploadResult imgResult = uploadFileCached(gPath, spec.categorySlug, uploadCache, order, spec.galleryPaths.size());
                    WorkImage workImage = new WorkImage(
                            imgResult.getSecureUrl(),
                            imgResult.getPublicId(),
                            spec.title + " - Image " + order,
                            order
                    );
                    work.addImage(workImage);
                    order++;
                }

                portfolioWorkRepository.save(work);
                logger.info("Successfully migrated work '{}' with {} gallery images.", spec.title, work.getImages().size());

            } catch (Exception e) {
                logger.error("Error migrating portfolio work '{}': {}", spec.title, e.getMessage(), e);
            }
        }

        logger.info("Portfolio migration completed successfully.");
    }

    private CloudinaryUploadResult uploadFileCached(String relativePath, String categorySlug, Map<String, CloudinaryUploadResult> cache, int currentImg, int totalImgs) {
        if (cache.containsKey(relativePath)) {
            return cache.get(relativePath);
        }

        logger.info("Uploading image {}/{}...", currentImg, totalImgs);
        File file = resolveSourceFile(relativePath);
        String targetFolder = "smile-studios/portfolio/" + categorySlug;
        CloudinaryUploadResult result = cloudinaryService.uploadImage(file, targetFolder);
        cache.put(relativePath, result);
        return result;
    }

    private File resolveSourceFile(String relativePath) {
        File file = new File(relativePath);
        if (file.exists()) {
            return file;
        }
        File parentRel = new File("../" + relativePath);
        if (parentRel.exists()) {
            return parentRel;
        }
        File absPath = new File("c:/study/learning/Smile_Studios_/" + relativePath);
        if (absPath.exists()) {
            return absPath;
        }
        return file;
    }
}

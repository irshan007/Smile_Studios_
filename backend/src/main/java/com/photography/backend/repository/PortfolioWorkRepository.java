package com.photography.backend.repository;

import com.photography.backend.entity.PortfolioWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioWorkRepository extends JpaRepository<PortfolioWork, Long> {

    Optional<PortfolioWork> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<PortfolioWork> findByIsPublishedTrueOrderByDisplayOrderAsc();

    List<PortfolioWork> findByCategoryIdAndIsPublishedTrueOrderByDisplayOrderAsc(Long categoryId);

    List<PortfolioWork> findByCategorySlugAndIsPublishedTrueOrderByDisplayOrderAsc(String categorySlug);

    List<PortfolioWork> findByIsFeaturedTrueAndIsPublishedTrueOrderByDisplayOrderAsc();

    List<PortfolioWork> findByShowInHeroTrueAndIsPublishedTrueOrderByDisplayOrderAsc();

    List<PortfolioWork> findByShowInSelectedWorksTrueAndIsPublishedTrueOrderByDisplayOrderAsc();
}

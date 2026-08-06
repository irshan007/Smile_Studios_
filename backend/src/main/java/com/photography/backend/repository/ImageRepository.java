package com.photography.backend.repository;

import com.photography.backend.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByCategoryOrderByDisplayOrderAsc(String category);

    List<Image> findByIsFeaturedTrueOrderByDisplayOrderAsc();

    List<Image> findByShowInHeroTrueOrderByHeroOrderAsc();

    List<Image> findByShowInSelectedWorksTrueOrderBySelectedWorksOrderAsc();

    @Query("SELECT DISTINCT i.category FROM Image i")
    List<String> findDistinctCategories();

    List<Image> findAllByOrderByDisplayOrderAsc();
}

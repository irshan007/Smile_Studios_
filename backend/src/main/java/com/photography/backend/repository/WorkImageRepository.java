package com.photography.backend.repository;

import com.photography.backend.entity.WorkImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkImageRepository extends JpaRepository<WorkImage, Long> {

    List<WorkImage> findByPortfolioWorkIdOrderByDisplayOrderAsc(Long portfolioWorkId);
}

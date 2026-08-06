package com.photography.backend.entity;

// Package fix
package com.photography.backend.repository;

import com.photography.backend.entity.ContactSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactSubmissionRepository extends JpaRepository<ContactSubmission, Long> {
    List<ContactSubmission> findAllByOrderByCreatedAtDesc();
}

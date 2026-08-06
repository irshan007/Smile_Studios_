package com.photography.backend.service;

import com.photography.backend.dto.ContactRequestDTO;
import com.photography.backend.entity.ContactSubmission;
import com.photography.backend.repository.ContactSubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactService {

    private final ContactSubmissionRepository contactSubmissionRepository;

    public ContactService(ContactSubmissionRepository contactSubmissionRepository) {
        this.contactSubmissionRepository = contactSubmissionRepository;
    }

    @Transactional
    public ContactSubmission processContactSubmission(ContactRequestDTO dto) {
        // Anti-spam Honeypot Check: if 'website' field is populated, silently handle without database write or error
        if (dto.getWebsite() != null && !dto.getWebsite().isBlank()) {
            // Fake success response for bots
            ContactSubmission dummy = new ContactSubmission(dto.getName(), dto.getEmail(), dto.getPhone(), dto.getEventDate(), dto.getMessage());
            return dummy;
        }

        ContactSubmission submission = new ContactSubmission(
                dto.getName(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getEventDate(),
                dto.getMessage()
        );

        return contactSubmissionRepository.save(submission);
    }
}

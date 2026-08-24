package com.photography.backend.service;

import com.photography.backend.dto.ContactRequestDTO;
import com.photography.backend.entity.ContactSubmission;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ContactService {

    private final List<ContactSubmission> submissions = new CopyOnWriteArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public ContactSubmission processContactSubmission(ContactRequestDTO dto) {
        // Anti-spam Honeypot Check: if 'website' field is populated, silently return fake submission without saving
        if (dto.getWebsite() != null && !dto.getWebsite().isBlank()) {
            ContactSubmission dummy = new ContactSubmission(dto.getName(), dto.getPhone(), dto.getEventDate(), dto.getMessage());
            dummy.setId(idCounter.getAndIncrement());
            return dummy;
        }

        ContactSubmission submission = new ContactSubmission(
                dto.getName(),
                dto.getPhone(),
                dto.getEventDate(),
                dto.getMessage()
        );
        submission.setId(idCounter.getAndIncrement());

        submissions.add(submission);
        return submission;
    }

    public List<ContactSubmission> getAllSubmissions() {
        return List.copyOf(submissions);
    }
}

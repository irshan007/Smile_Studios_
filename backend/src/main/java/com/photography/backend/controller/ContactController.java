package com.photography.backend.controller;

import com.photography.backend.dto.ApiResponseDTO;
import com.photography.backend.dto.ContactRequestDTO;
import com.photography.backend.entity.ContactSubmission;
import com.photography.backend.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<ContactSubmission>> submitContactForm(@Valid @RequestBody ContactRequestDTO dto) {
        ContactSubmission saved = contactService.processContactSubmission(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Thank you! Your message has been received.", saved));
    }
}

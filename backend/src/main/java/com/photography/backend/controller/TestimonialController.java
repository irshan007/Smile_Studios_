package com.photography.backend.controller;

import com.photography.backend.dto.ApiResponseDTO;
import com.photography.backend.entity.Testimonial;
import com.photography.backend.service.TestimonialService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/testimonials")
public class TestimonialController {

    private final TestimonialService testimonialService;

    public TestimonialController(TestimonialService testimonialService) {
        this.testimonialService = testimonialService;
    }

    @GetMapping
    public ApiResponseDTO<List<Testimonial>> getTestimonials() {
        List<Testimonial> testimonials = testimonialService.getAllTestimonials();
        return ApiResponseDTO.ok("Testimonials retrieved successfully", testimonials);
    }
}

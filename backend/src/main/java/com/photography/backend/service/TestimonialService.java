package com.photography.backend.service;

import com.photography.backend.entity.Testimonial;
import com.photography.backend.repository.TestimonialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestimonialService {

    private final TestimonialRepository testimonialRepository;

    public TestimonialService(TestimonialRepository testimonialRepository) {
        this.testimonialRepository = testimonialRepository;
    }

    @Transactional(readOnly = true)
    public List<Testimonial> getAllTestimonials() {
        return testimonialRepository.findAllByOrderByDisplayOrderAsc();
    }
}

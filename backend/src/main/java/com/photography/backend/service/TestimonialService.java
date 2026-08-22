package com.photography.backend.service;

import com.photography.backend.entity.Testimonial;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class TestimonialService {

    private final List<Testimonial> testimonials = new CopyOnWriteArrayList<>();

    public TestimonialService() {
        seedTestimonials();
    }

    private void seedTestimonials() {
        testimonials.addAll(Arrays.asList(
                new Testimonial(1L, "Ananya & Siddharth", "Smile Studios captured every emotion of our 3-day wedding so effortlessly. Looking back at our album feels like reliving the magic all over again!", 1),
                new Testimonial(2L, "Priya & Vikram", "Absolute masterclass in storytelling. Their attention to detail, lighting, and genuine moments made our photos look straight out of a luxury fashion magazine.", 2),
                new Testimonial(3L, "Rohan Mehta", "We hired Smile Studios for our corporate gala and executive portraits. Their professionalism, prompt delivery, and aesthetic quality were top notch!", 3),
                new Testimonial(4L, "Meera & Arjun", "Our maternity session turned out beyond our wildest dreams. Soft, elegant, and timeless pictures that we will treasure forever.", 4)
        ));
    }

    public List<Testimonial> getAllTestimonials() {
        return testimonials.stream()
                .sorted(Comparator.comparingInt(t -> t.getDisplayOrder() != null ? t.getDisplayOrder() : 0))
                .toList();
    }
}

package com.photography.backend.config;

import com.photography.backend.entity.Image;
import com.photography.backend.entity.Testimonial;
import com.photography.backend.repository.ImageRepository;
import com.photography.backend.repository.TestimonialRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ImageRepository imageRepository;
    private final TestimonialRepository testimonialRepository;

    public DataSeeder(ImageRepository imageRepository, TestimonialRepository testimonialRepository) {
        this.imageRepository = imageRepository;
        this.testimonialRepository = testimonialRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (imageRepository.count() == 0) {
            seedImages();
        }

        if (testimonialRepository.count() == 0) {
            seedTestimonials();
        }
    }

    private void seedImages() {
        List<Image> sampleImages = Arrays.asList(
                // Hero Slideshow & Featured Portfolios (showInHero = true)
                new Image("https://images.unsplash.com/photo-1519741497674-611481863552?auto=format&fit=crop&q=80&w=1800", "hero_1", "Portraits", "Elegant Fine Art Portrait", 1, true, true, true, 1, 1),
                new Image("https://images.unsplash.com/photo-1511285560929-80b456fea0bc?auto=format&fit=crop&q=80&w=1800", "hero_2", "Portraits", "Luxury Sunset Couple Portrait", 2, true, true, true, 2, 2),
                new Image("https://images.unsplash.com/photo-1583939003579-730e3918a45a?auto=format&fit=crop&q=80&w=1800", "hero_3", "Portraits", "Intimate Studio Moment", 3, true, true, true, 3, 3),
                new Image("https://images.unsplash.com/photo-1606800052052-a08af7148866?auto=format&fit=crop&q=80&w=1800", "hero_4", "Portraits", "Editorial Fashion Portraiture", 4, true, true, true, 4, 4),

                // Pre Weddings
                new Image("https://images.unsplash.com/photo-1520854221256-17451cc331bf?auto=format&fit=crop&q=80&w=1800", "pw_1", "Pre Weddings", "Beachside Sunset Pre Wedding", 1, true, false, true, 0, 5),
                new Image("https://images.unsplash.com/photo-1532712938310-34cb3982ef74?auto=format&fit=crop&q=80&w=1800", "pw_2", "Pre Weddings", "Heritage Palace Romance", 2, true, false, true, 0, 6),
                new Image("https://images.unsplash.com/photo-1465495976277-4387d4b0b4c6?auto=format&fit=crop&q=80&w=1800", "pw_3", "Pre Weddings", "Mountain View Couple", 3, false, false, false, 0, 0),

                // Tamil Weddings
                new Image("https://images.unsplash.com/photo-1610030469983-98e550d6193c?auto=format&fit=crop&q=80&w=1800", "tw_1", "Tamil Weddings", "Traditional Muhurtham Ritual", 1, true, false, true, 0, 7),
                new Image("https://images.unsplash.com/photo-1544078751-58fee2d8a03b?auto=format&fit=crop&q=80&w=1800", "tw_2", "Tamil Weddings", "Kanjivaram Bride Elegance", 2, true, false, false, 0, 0),

                // Telugu Weddings
                new Image("https://images.unsplash.com/photo-1545232979-fbfd42e2006f?auto=format&fit=crop&q=80&w=1800", "tel_1", "Telugu Weddings", "Jeelakarra Bellam Moment", 1, true, false, true, 0, 8),

                // Brahmin Weddings
                new Image("https://images.unsplash.com/photo-1511285560929-80b456fea0bc?auto=format&fit=crop&q=80&w=1800", "bw_1", "Brahmin Weddings", "Oonjal Swing Ceremony", 1, true, false, false, 0, 0),

                // Christian Weddings
                new Image("https://images.unsplash.com/photo-1519225421980-715cb0215aed?auto=format&fit=crop&q=80&w=1800", "cw_1", "Christian Weddings", "Cathedral Gown Walk", 1, true, false, false, 0, 0),

                // Muslim Weddings
                new Image("https://images.unsplash.com/photo-1532712938310-34cb3982ef74?auto=format&fit=crop&q=80&w=1800", "mw_1", "Muslim Weddings", "Nikah Ceremony Grace", 1, true, false, false, 0, 0),

                // Engagement
                new Image("https://images.unsplash.com/photo-1465495976277-4387d4b0b4c6?auto=format&fit=crop&q=80&w=1800", "eng_1", "Engagement", "Ring Ceremony Toast", 1, true, false, false, 0, 0),

                // Events
                new Image("https://images.unsplash.com/photo-1511795409834-ef04bbd61622?auto=format&fit=crop&q=80&w=1800", "evt_1", "Events", "Gala Stage Lighting", 1, true, false, true, 0, 9),
                new Image("https://images.unsplash.com/photo-1492684223066-81342ee5ff30?auto=format&fit=crop&q=80&w=1800", "evt_2", "Events", "Celebration Atmosphere", 2, false, false, false, 0, 0),

                // Maternity/Baby
                new Image("https://images.unsplash.com/photo-1555252333-9f8e92e65df9?auto=format&fit=crop&q=80&w=1800", "mat_1", "Maternity/Baby", "Maternity Glow Portrait", 1, true, false, true, 0, 10),
                new Image("https://images.unsplash.com/photo-1519689680058-324335c77eba?auto=format&fit=crop&q=80&w=1800", "mat_2", "Maternity/Baby", "Newborn Soft Moments", 2, false, false, false, 0, 0)
        );

        imageRepository.saveAll(sampleImages);
    }

    private void seedTestimonials() {
        List<Testimonial> sampleTestimonials = Arrays.asList(
                new Testimonial("Ananya & Siddharth", "Smile Studios captured every emotion of our 3-day wedding so effortlessly. Looking back at our album feels like reliving the magic all over again!", 1),
                new Testimonial("Priya & Vikram", "Absolute masterclass in storytelling. Their attention to detail, lighting, and genuine moments made our photos look straight out of a luxury fashion magazine.", 2),
                new Testimonial("Rohan Mehta", "We hired Smile Studios for our corporate gala and executive portraits. Their professionalism, prompt delivery, and aesthetic quality were top notch!", 3),
                new Testimonial("Meera & Arjun", "Our maternity session turned out beyond our wildest dreams. Soft, elegant, and timeless pictures that we will treasure forever.", 4)
        );

        testimonialRepository.saveAll(sampleTestimonials);
    }
}

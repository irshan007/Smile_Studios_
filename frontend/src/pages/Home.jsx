import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { ChevronLeft, ChevronRight, ArrowRight, Camera, Heart, Sparkles } from 'lucide-react';
import Lightbox from 'yet-another-react-lightbox';
import 'yet-another-react-lightbox/styles.css';

import { SeoHead } from '../components/SeoHead';
import { CloudinaryImage } from '../components/CloudinaryImage';
import { fetchApi } from '../utils/api';
import { heroSlides, homeSelectedWorks, aboutGalleryImages } from '../data/portfolioAssets';
import './Home.css';

const DEFAULT_HERO_SLIDES = heroSlides;

export function Home() {
  const [activeSlide, setActiveSlide] = useState(0);
  const [heroImages, setHeroImages] = useState([]);
  const [selectedWorks, setSelectedWorks] = useState([]);
  const [testimonials, setTestimonials] = useState([]);
  const [testimonialIdx, setTestimonialIdx] = useState(0);
  const [lightboxOpen, setLightboxOpen] = useState(false);
  const [lightboxIdx, setLightboxIdx] = useState(0);

  // Auto-rotating hero slideshow interval (5 seconds)
  const slideCount = heroImages.length > 0 ? heroImages.length : DEFAULT_HERO_SLIDES.length;

  useEffect(() => {
    const timer = setInterval(() => {
      setActiveSlide((prev) => (prev + 1) % slideCount);
    }, 5000);
    return () => clearInterval(timer);
  }, [slideCount]);

  // Fetch Hero, Selected Works & Testimonials from Backend API
  useEffect(() => {
    async function loadHomeData() {
      try {
        const heroRes = await fetchApi('/gallery/hero');
        if (heroRes?.data?.length > 0) {
          setHeroImages(heroRes.data);
        }
      } catch (err) {
        console.warn('Backend offline or loading fallback hero slides:', err);
      }

      try {
        const worksRes = await fetchApi('/gallery/selected-works');
        if (worksRes?.data?.length > 0) {
          setSelectedWorks(worksRes.data);
        }
      } catch (err) {
        console.warn('Backend offline or loading fallback selected works:', err);
      }

      try {
        const testRes = await fetchApi('/testimonials');
        if (testRes?.data?.length > 0) {
          setTestimonials(testRes.data);
        }
      } catch (err) {
        console.warn('Backend offline or loading fallback testimonials:', err);
      }
    }
    loadHomeData();
  }, []);

  // Display slides logic
  const displayHeroSlides = heroImages.length > 0
    ? heroImages.map(img => ({
        url: img.url,
        title: img.altText || 'Smile Studios Fine Art Photography',
        tag: img.category ? `Smile Studios • ${img.category}` : 'Smile Studios Photography & Films',
      }))
    : DEFAULT_HERO_SLIDES.map(slide => ({ url: slide.src, title: slide.title, tag: slide.tag }));

  const displayWorks = selectedWorks.length > 0
    ? selectedWorks
    : homeSelectedWorks.map((item) => ({ id: item.id, url: item.src, category: item.category, altText: item.altText }));

  const displayTestimonials = testimonials.length > 0 ? testimonials : [
    { id: 1, coupleNames: 'Ananya & Siddharth', message: 'Every detail felt intentional, warm and effortless. Our album is beautiful because it captured the real feeling of our day.' },
    { id: 2, coupleNames: 'Priya & Vikram', message: 'The team made us feel completely at ease. The final gallery feels elegant, honest and timeless.' },
  ];

  const serviceHighlights = [
    {
      title: 'Wedding Storytelling',
      description: 'From ceremonies to late-night celebrations, we document the emotion, ritual and atmosphere in a way that feels cinematic and personal.',
      image: heroSlides[0].src,
    },
    {
      title: 'Editorial Portraits',
      description: 'Thoughtful portraits that balance confidence, softness and character, crafted for modern couples and individuals alike.',
      image: homeSelectedWorks[5].src,
    },
    {
      title: 'Family Milestones',
      description: 'Maternity, baby and family sessions designed to feel calm, intimate and beautifully natural.',
      image: aboutGalleryImages[2],
    },
  ];

  return (
    <>
      <SeoHead
        title="Smile Studios | Luxury Photography & Cinematography"
        description="A refined photography studio creating timeless wedding, portrait, maternity and event stories with warmth, elegance and intention."
      />

      {/* 1. HERO SLIDESHOW */}
      <section className="hero-slider">
        {displayHeroSlides.map((slide, idx) => (
          <div
            key={idx}
            className={`hero-slide ${idx === activeSlide ? 'active' : ''}`}
          >
            <img src={slide.url} alt={slide.title} className="hero-slide-bg" />
            <div className="hero-overlay">
              <div className="container">
                <div className="hero-content">
                  <span className="hero-tag">{slide.tag}</span>
                  <h1 className="hero-title">{slide.title}</h1>
                  <div style={{ display: 'flex', gap: '1.25rem' }}>
                    <Link to="/portfolio" className="btn-primary">
                      Explore Portfolio <ArrowRight size={18} />
                    </Link>
                    <Link to="/contact" className="btn-outline">
                      Book Consultation
                    </Link>
                  </div>
                </div>
              </div>
            </div>
          </div>
        ))}

        {/* Hero Arrow Controls */}
        <div className="hero-controls">
          <button
            className="hero-arrow-btn"
            onClick={() => setActiveSlide((prev) => (prev - 1 + displayHeroSlides.length) % displayHeroSlides.length)}
            aria-label="Previous slide"
          >
            <ChevronLeft size={22} />
          </button>
          <button
            className="hero-arrow-btn"
            onClick={() => setActiveSlide((prev) => (prev + 1) % displayHeroSlides.length)}
            aria-label="Next slide"
          >
            <ChevronRight size={22} />
          </button>
        </div>
      </section>

      {/* 2. WHY US SECTION */}
      <section className="section-padding container">
        <div className="why-us-grid">
          <div className="why-us-images">
            <CloudinaryImage
              src={aboutGalleryImages[0]}
              alt="A polished wedding portrait from Smile Studios"
              className="why-us-img-1"
            />
            <CloudinaryImage
              src={aboutGalleryImages[1]}
              alt="An engagement session captured with natural intimacy"
              className="why-us-img-2"
            />
          </div>

          <div>
            <span style={{ textTransform: 'uppercase', letterSpacing: '0.25em', color: 'var(--accent-gold)', fontSize: '0.85rem', fontWeight: 600 }}>
              SMILE STUDIOS PHILOSOPHY
            </span>
            <h2 style={{ marginTop: '0.5rem', marginBottom: '1.5rem' }}>
              A studio that photographs real love, quiet luxury, and meaningful milestones.
            </h2>
            <p style={{ marginBottom: '1.25rem' }}>
              Smile Studios is known for warm, graceful storytelling that feels both polished and deeply personal. We create imagery for weddings, engagement sessions, editorial portraits, maternity moments and intimate celebrations with a timeless, human touch.
            </p>
            <p style={{ marginBottom: '2rem' }}>
              Our approach is calm, detail-led and intentionally unforced, so every gallery feels like you — elegant, relaxed and completely true to the moment.
            </p>

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.5rem', marginBottom: '2rem' }}>
              <div style={{ display: 'flex', gap: '0.75rem', alignItems: 'flex-start' }}>
                <Camera color="var(--accent-gold)" size={24} style={{ flexShrink: 0 }} />
                <div>
                  <h4 style={{ color: 'var(--text-main)', fontSize: '1.05rem', marginBottom: '0.25rem' }}>Tailored for every chapter</h4>
                  <p style={{ fontSize: '0.85rem' }}>Weddings, engagements, family milestones and editorial portraits.</p>
                </div>
              </div>
              <div style={{ display: 'flex', gap: '0.75rem', alignItems: 'flex-start' }}>
                <Sparkles color="var(--accent-gold)" size={24} style={{ flexShrink: 0 }} />
                <div>
                  <h4 style={{ color: 'var(--text-main)', fontSize: '1.05rem', marginBottom: '0.25rem' }}>Refined storytelling</h4>
                  <p style={{ fontSize: '0.85rem' }}>Soft light, elegant composition and a calm client experience.</p>
                </div>
              </div>
            </div>

            <Link to="/about" className="btn-outline">
              Discover Our Studio
            </Link>
          </div>
        </div>
      </section>

      {/* 3. SELECTED WORKS PREVIEW */}
      <section className="featured-stories-section" style={{ background: 'var(--bg-surface)', padding: '6rem 0', borderTop: '1px solid var(--border-light)', borderBottom: '1px solid var(--border-light)' }}>
        <div className="container">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', marginBottom: '3rem', flexWrap: 'wrap', gap: '1rem' }}>
            <div>
              <span style={{ textTransform: 'uppercase', letterSpacing: '0.25em', color: 'var(--accent-gold)', fontSize: '0.85rem', fontWeight: 600 }}>
                FEATURED STORIES
              </span>
              <h2>Selected Works</h2>
            </div>
            <Link to="/portfolio" className="btn-outline" style={{ padding: '0.6rem 1.5rem', fontSize: '0.8rem' }}>
              View All Galleries <ArrowRight size={16} />
            </Link>
          </div>

          <div className="works-grid">
            {displayWorks.map((img, index) => (
              <div
                key={img.id || index}
                className="work-card"
                onClick={() => {
                  setLightboxIdx(index);
                  setLightboxOpen(true);
                }}
              >
                <CloudinaryImage src={img.url} alt={img.altText || img.category} />
                <div className="work-card-overlay">
                  <span style={{ color: 'var(--accent-gold)', fontSize: '0.8rem', textTransform: 'uppercase', letterSpacing: '0.15em' }}>
                    {img.category || 'Portfolio'}
                  </span>
                  <h3 style={{ fontSize: '1.4rem', fontFamily: 'var(--font-serif)', color: 'var(--text-main)' }}>
                    {img.altText || 'View Photograph'}
                  </h3>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* 4. SIGNATURE SERVICES */}
      <section className="section-padding container">
        <div style={{ textAlign: 'center', marginBottom: '3rem' }}>
          <span style={{ textTransform: 'uppercase', letterSpacing: '0.25em', color: 'var(--accent-gold)', fontSize: '0.85rem', fontWeight: 600 }}>
            WHAT WE CAPTURE
          </span>
          <h2>Photography shaped around your story</h2>
        </div>

        <div className="service-grid">
          {serviceHighlights.map((service) => (
            <div key={service.title} className="service-card">
              <CloudinaryImage src={service.image} alt={service.title} className="service-image" />
              <div className="service-card-content">
                <h3>{service.title}</h3>
                <p>{service.description}</p>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* 5. TESTIMONIALS CAROUSEL */}
      <section className="section-padding container">
        <div style={{ textAlign: 'center', marginBottom: '3.5rem' }}>
          <Heart color="var(--accent-gold)" size={28} style={{ marginBottom: '0.75rem' }} />
          <span style={{ textTransform: 'uppercase', letterSpacing: '0.25em', color: 'var(--accent-gold)', fontSize: '0.85rem', fontWeight: 600, display: 'block' }}>
            KIND WORDS
          </span>
          <h2>Client Stories</h2>
        </div>

        <div className="testimonial-card">
          <p className="testimonial-quote">
            "{displayTestimonials[testimonialIdx]?.message}"
          </p>
          <span className="testimonial-names">
            — {displayTestimonials[testimonialIdx]?.coupleNames}
          </span>

          <div style={{ display: 'flex', justifyContent: 'center', gap: '1rem', marginTop: '2.5rem' }}>
            <button
              className="hero-arrow-btn"
              style={{ width: '40px', height: '40px' }}
              onClick={() => setTestimonialIdx((prev) => (prev - 1 + displayTestimonials.length) % displayTestimonials.length)}
              aria-label="Previous testimonial"
            >
              <ChevronLeft size={18} />
            </button>
            <button
              className="hero-arrow-btn"
              style={{ width: '40px', height: '40px' }}
              onClick={() => setTestimonialIdx((prev) => (prev + 1) % displayTestimonials.length)}
              aria-label="Next testimonial"
            >
              <ChevronRight size={18} />
            </button>
          </div>
        </div>
      </section>

      {/* 6. CLOSING CTA BANNER */}
      <section className="closing-cta">
        <div className="container" style={{ maxWidth: '700px' }}>
          <span style={{ textTransform: 'uppercase', letterSpacing: '0.25em', color: 'var(--accent-gold)', fontSize: '0.85rem', fontWeight: 600 }}>
            BEGIN YOUR STORY
          </span>
          <h2 style={{ marginTop: '0.5rem', marginBottom: '1.5rem', fontSize: '3rem' }}>
            Let’s begin your next chapter together.
          </h2>
          <p style={{ marginBottom: '2.5rem' }}>
            We are currently booking weddings, engagement sessions, editorial portraits, family milestones and cinematic event films across India and internationally.
          </p>
          <Link to="/contact" className="btn-primary">
            Request Availability & Pricing <ArrowRight size={18} />
          </Link>
        </div>
      </section>

      {/* LIGHTBOX POPUP */}
      <Lightbox
        open={lightboxOpen}
        close={() => setLightboxOpen(false)}
        index={lightboxIdx}
        slides={displayWorks.map((img) => ({ src: img.url, alt: img.altText }))}
      />
    </>
  );
}

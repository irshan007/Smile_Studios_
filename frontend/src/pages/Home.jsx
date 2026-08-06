import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { ChevronLeft, ChevronRight, ArrowRight, Camera, Award, Heart, Sparkles } from 'lucide-react';
import Lightbox from 'yet-another-react-lightbox';
import 'yet-another-react-lightbox/styles.css';

import { SeoHead } from '../components/SeoHead';
import { CloudinaryImage } from '../components/CloudinaryImage';
import { fetchApi } from '../utils/api';
import './Home.css';

const DEFAULT_HERO_SLIDES = [
  {
    url: 'https://images.unsplash.com/photo-1519741497674-611481863552?auto=format&fit=crop&q=80&w=2000',
    title: 'Timeless Romance, Fine Art Vision',
    tag: 'Smile Studios • Photography & Films',
  },
  {
    url: 'https://images.unsplash.com/photo-1511285560929-80b456fea0bc?auto=format&fit=crop&q=80&w=2000',
    title: 'Unscripted Moments, High Fashion Elegance',
    tag: 'Fine Art Portraits & Weddings',
  },
  {
    url: 'https://images.unsplash.com/photo-1583939003579-730e3918a45a?auto=format&fit=crop&q=80&w=2000',
    title: 'Events, Celebrations & Life Stories',
    tag: 'Commercial Events & Family Heirlooms',
  },
  {
    url: 'https://images.unsplash.com/photo-1606800052052-a08af7148866?auto=format&fit=crop&q=80&w=2000',
    title: 'Crafted for the Discerning Client',
    tag: 'Full-Service Photography Studio',
  },
];

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
    : DEFAULT_HERO_SLIDES;

  const displayWorks = selectedWorks.length > 0 ? selectedWorks : [
    { id: 1, url: 'https://images.unsplash.com/photo-1519741497674-611481863552?auto=format&fit=crop&q=80&w=1200', category: 'Portraits', altText: 'Fine Art Portrait' },
    { id: 2, url: 'https://images.unsplash.com/photo-1511285560929-80b456fea0bc?auto=format&fit=crop&q=80&w=1200', category: 'Pre Weddings', altText: 'Sunset Romance' },
    { id: 3, url: 'https://images.unsplash.com/photo-1610030469983-98e550d6193c?auto=format&fit=crop&q=80&w=1200', category: 'Tamil Weddings', altText: 'Muhurtham Ritual' },
    { id: 4, url: 'https://images.unsplash.com/photo-1511795409834-ef04bbd61622?auto=format&fit=crop&q=80&w=1200', category: 'Events', altText: 'Gala Night Atmosphere' },
    { id: 5, url: 'https://images.unsplash.com/photo-1555252333-9f8e92e65df9?auto=format&fit=crop&q=80&w=1200', category: 'Maternity/Baby', altText: 'Maternity Glow' },
    { id: 6, url: 'https://images.unsplash.com/photo-1532712938310-34cb3982ef74?auto=format&fit=crop&q=80&w=1200', category: 'Muslim Weddings', altText: 'Nikah Grace' },
  ];

  const displayTestimonials = testimonials.length > 0 ? testimonials : [
    { id: 1, coupleNames: 'Ananya & Siddharth', message: 'Smile Studios captured every emotion of our 3-day wedding so effortlessly. Looking back at our album feels like reliving the magic all over again!' },
    { id: 2, coupleNames: 'Priya & Vikram', message: 'Absolute masterclass in storytelling. Their attention to detail, lighting, and genuine moments made our photos look straight out of a luxury magazine.' },
  ];

  return (
    <>
      <SeoHead
        title="Smile Studios | Luxury Photography & Cinematography"
        description="Award-winning full-service photography studio specializing in weddings, portraits, pre-weddings, corporate events, maternity/baby sessions, and films."
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
              src="https://images.unsplash.com/photo-1519741497674-611481863552?auto=format&fit=crop&q=80&w=1000"
              alt="Portrait Session"
              className="why-us-img-1"
            />
            <CloudinaryImage
              src="https://images.unsplash.com/photo-1520854221256-17451cc331bf?auto=format&fit=crop&q=80&w=1000"
              alt="Couple Embrace"
              className="why-us-img-2"
            />
          </div>

          <div>
            <span style={{ textTransform: 'uppercase', letterSpacing: '0.25em', color: 'var(--accent-gold)', fontSize: '0.85rem', fontWeight: 600 }}>
              SMILE STUDIOS PHILOSOPHY
            </span>
            <h2 style={{ marginTop: '0.5rem', marginBottom: '1.5rem' }}>
              Full-Service Fine Art Photography & Cinematography Studio
            </h2>
            <p style={{ marginBottom: '1.25rem' }}>
              Smile Studios is a boutique creative studio capturing life's grandest milestones and subtle emotions. From high-fashion editorial portraits and luxury destination weddings to corporate galas and intimate maternity sessions, our visual storytelling is tailored to your unique vision.
            </p>
            <p style={{ marginBottom: '2rem' }}>
              Our team combines medium-format optical precision with custom editorial color grading, delivering heirlooms that remain timeless for generations.
            </p>

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.5rem', marginBottom: '2rem' }}>
              <div style={{ display: 'flex', gap: '0.75rem', alignItems: 'flex-start' }}>
                <Camera color="var(--accent-gold)" size={24} style={{ flexShrink: 0 }} />
                <div>
                  <h4 style={{ color: 'var(--text-main)', fontSize: '1.05rem', marginBottom: '0.25rem' }}>Multi-Disciplinary Scope</h4>
                  <p style={{ fontSize: '0.85rem' }}>Weddings, Portraits, Events, Maternity & Films.</p>
                </div>
              </div>
              <div style={{ display: 'flex', gap: '0.75rem', alignItems: 'flex-start' }}>
                <Sparkles color="var(--accent-gold)" size={24} style={{ flexShrink: 0 }} />
                <div>
                  <h4 style={{ color: 'var(--text-main)', fontSize: '1.05rem', marginBottom: '0.25rem' }}>Award-Winning Team</h4>
                  <p style={{ fontSize: '0.85rem' }}>Recognized across top visual arts publications.</p>
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
      <section style={{ background: 'var(--bg-surface)', padding: '6rem 0', borderTop: '1px solid var(--border-light)', borderBottom: '1px solid var(--border-light)' }}>
        <div className="container">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', marginBottom: '3rem', flexWrap: 'wrap', gap: '1rem' }}>
            <div>
              <span style={{ textTransform: 'uppercase', letterSpacing: '0.25em', color: 'var(--accent-gold)', fontSize: '0.85rem', fontWeight: 600 }}>
                CURATED SELECTION
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

      {/* 4. TESTIMONIALS CAROUSEL */}
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

      {/* 5. CLOSING CTA BANNER */}
      <section className="closing-cta">
        <div className="container" style={{ maxWidth: '700px' }}>
          <span style={{ textTransform: 'uppercase', letterSpacing: '0.25em', color: 'var(--accent-gold)', fontSize: '0.85rem', fontWeight: 600 }}>
            BEGIN YOUR STORY
          </span>
          <h2 style={{ marginTop: '0.5rem', marginBottom: '1.5rem', fontSize: '3rem' }}>
            Let's Capture Your Next Chapter
          </h2>
          <p style={{ marginBottom: '2.5rem' }}>
            Currently accepting commissions for weddings, portraits, events, maternity sessions, and films across India and worldwide.
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

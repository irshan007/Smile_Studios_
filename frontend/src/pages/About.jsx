import React from 'react';
import { Link } from 'react-router-dom';
import { Camera, Sparkles, HeartHandshake, ArrowRight } from 'lucide-react';
import { SeoHead } from '../components/SeoHead';
import { CloudinaryImage } from '../components/CloudinaryImage';
import { aboutGalleryImages } from '../data/portfolioAssets';

export function About() {
  return (
    <>
      <SeoHead
        title="About Smile Studios"
        description="Learn how Smile Studios creates refined wedding, portrait and family imagery with warmth, artistry and a deeply personal process."
      />

      <div className="page-header">
        <div className="container">
          <span className="subtitle">ABOUT SMILE STUDIOS</span>
          <h1>Artistic Vision & Craftsmanship</h1>
        </div>
      </div>

      <section className="section-padding container">
        <div className="why-us-grid">
          <div>
            <span style={{ textTransform: 'uppercase', letterSpacing: '0.2em', color: 'var(--accent-gold)', fontSize: '0.85rem', fontWeight: 600 }}>
              OUR JOURNEY
            </span>
            <h2 style={{ marginTop: '0.5rem', marginBottom: '1.5rem' }}>
              We create imagery that feels timeless, personal and unmistakably yours.
            </h2>
            <p style={{ marginBottom: '1.25rem' }}>
              Smile Studios was built around the belief that the best photographs are the ones that feel effortless in the moment and timeless in hindsight. We work with couples, families and individuals who want their story captured with warmth, elegance and honesty.
            </p>
            <p style={{ marginBottom: '1.25rem' }}>
              From intimate weddings and engagement sessions to editorial portraits and family milestones, our work is shaped by thoughtful composition, natural light and a calm, relaxed experience from start to finish.
            </p>
            <p style={{ marginBottom: '2rem' }}>
              Our goal is simple: to leave you with photographs that feel as meaningful years from now as they did the day they were captured.
            </p>
          </div>

          <div style={{ position: 'relative', height: '500px' }}>
            <CloudinaryImage
              src={aboutGalleryImages[0]}
              alt="A Smile Studios portrait session in natural light"
              style={{ width: '100%', height: '100%', borderRadius: 'var(--radius-md)', objectFit: 'cover' }}
            />
          </div>
        </div>
      </section>

      {/* Core Pillars */}
      <section style={{ background: 'var(--bg-surface)', padding: '6rem 0', borderTop: '1px solid var(--border-light)', borderBottom: '1px solid var(--border-light)' }}>
        <div className="container">
          <div style={{ textAlign: 'center', marginBottom: '4rem' }}>
            <span style={{ textTransform: 'uppercase', letterSpacing: '0.25em', color: 'var(--accent-gold)', fontSize: '0.85rem', fontWeight: 600 }}>
              WHAT SETS US APART
            </span>
            <h2>Our Core Principles</h2>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '2.5rem' }}>
            <div style={{ background: 'var(--bg-card)', padding: '2.5rem', borderRadius: 'var(--radius-md)', border: '1px solid var(--border-light)' }}>
              <Camera color="var(--accent-gold)" size={32} style={{ marginBottom: '1.25rem' }} />
              <h3 style={{ fontSize: '1.4rem', color: 'var(--text-main)', marginBottom: '0.75rem' }}>Medium Format Precision</h3>
              <p style={{ fontSize: '0.9rem' }}>
                We utilize Hasselblad and Leica systems with prime optics for unmatched resolution, skin-tone accuracy, and breathtaking dynamic range.
              </p>
            </div>

            <div style={{ background: 'var(--bg-card)', padding: '2.5rem', borderRadius: 'var(--radius-md)', border: '1px solid var(--border-light)' }}>
              <Sparkles color="var(--accent-gold)" size={32} style={{ marginBottom: '1.25rem' }} />
              <h3 style={{ fontSize: '1.4rem', color: 'var(--text-main)', marginBottom: '0.75rem' }}>Bespoke Color Grading</h3>
              <p style={{ fontSize: '0.9rem' }}>
                Every single photograph undergoes master color grading by hand — avoiding filter trends so your photos look timeless decades from now.
              </p>
            </div>

            <div style={{ background: 'var(--bg-card)', padding: '2.5rem', borderRadius: 'var(--radius-md)', border: '1px solid var(--border-light)' }}>
              <HeartHandshake color="var(--accent-gold)" size={32} style={{ marginBottom: '1.25rem' }} />
              <h3 style={{ fontSize: '1.4rem', color: 'var(--text-main)', marginBottom: '0.75rem' }}>Bespoke Client Experience</h3>
              <p style={{ fontSize: '0.9rem' }}>
                From pre-shoot moodboarding and styling guidance to custom hand-bound Italian leather albums, we provide an end-to-end luxury experience.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="section-padding container" style={{ textAlign: 'center' }}>
        <h2 style={{ marginBottom: '1.5rem' }}>Ready to Create Something Beautiful?</h2>
        <p style={{ maxWidth: '600px', margin: '0 auto 2.5rem' }}>
          Explore our portfolio or contact Smile Studios to reserve your dates.
        </p>
        <div style={{ display: 'flex', justifyContent: 'center', gap: '1.25rem' }}>
          <Link to="/portfolio" className="btn-primary">
            View Portfolio <ArrowRight size={18} />
          </Link>
          <Link to="/contact" className="btn-outline">
            Get In Touch
          </Link>
        </div>
      </section>
    </>
  );
}

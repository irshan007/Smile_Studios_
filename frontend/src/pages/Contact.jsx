import React from 'react';
import { Phone, MapPin, MessageCircle } from 'lucide-react';
import { SeoHead } from '../components/SeoHead';
import './Contact.css';

const InstagramIcon = ({ size = 22, color = 'currentColor' }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke={color} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <rect x="2" y="2" width="20" height="20" rx="5" ry="5"></rect>
    <path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z"></path>
    <line x1="17.5" y1="6.5" x2="17.51" y2="6.5"></line>
  </svg>
);

const FacebookIcon = ({ size = 22, color = 'currentColor' }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke={color} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <path d="M18 2h-3a5 5 0 0 0-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 0 1 1-1h3z"></path>
  </svg>
);

const YoutubeIcon = ({ size = 22, color = 'currentColor' }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke={color} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <path d="M2.5 17a24.12 24.12 0 0 1 0-10 2 2 0 0 1 1.4-1.4 49.56 49.56 0 0 1 16.2 0A2 2 0 0 1 21.5 7a24.12 24.12 0 0 1 0 10 2 2 0 0 1-1.4 1.4 49.55 49.55 0 0 1-16.2 0A2 2 0 0 1 2.5 17"></path>
    <path d="m10 15 5-3-5-3z"></path>
  </svg>
);

export function Contact() {
  return (
    <>
      <SeoHead
        title="Contact Studio"
        description="Book a consultation for your upcoming wedding or editorial portrait session."
      />

      <div className="page-header">
        <div className="container">
          <span className="subtitle">RESERVE YOUR DATES</span>
          <h1>Get In Touch</h1>
        </div>
      </div>

      <section className="section-padding container">
        <div style={{ maxWidth: '720px', margin: '0 auto' }}>
          <span className="section-label">
            LET'S TALK
          </span>
          <h2 style={{ marginTop: '0.5rem', marginBottom: '1.5rem' }}>
            We Would Love To Be Part Of Your Story
          </h2>
          <p style={{ marginBottom: '2.5rem' }}>
            Reach out with your event details, location, and dates. We typically respond within 24 hours to schedule an initial consultation call.
          </p>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '1.75rem' }}>
            <div style={{ display: 'flex', gap: '1rem', alignItems: 'flex-start' }}>
              <a href="tel:+917200039833" className="social-icon-btn" style={{ width: '50px', height: '50px', flexShrink: 0 }} aria-label="Call +917200039833">
                <Phone size={22} color="var(--accent-gold)" />
              </a>
              <div>
                <h4 style={{ color: 'var(--text-main)', fontSize: '1.05rem' }}>Phone & WhatsApp</h4>
                <p style={{ fontSize: '0.9rem' }}>
                  <a href="tel:+917200039833" style={{ color: 'inherit' }}>+917200039833</a>
                </p>
                <p style={{ fontSize: '0.8rem', color: 'var(--text-dim)' }}>
                  WhatsApp: <a href="https://wa.me/917200039833" target="_blank" rel="noreferrer" style={{ color: 'var(--accent-gold)' }}>Smile Studios</a>
                </p>
              </div>
            </div>

            <div style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
              <a href="https://instagram.com/smilestudios_official" target="_blank" rel="noreferrer" className="social-icon-btn" style={{ width: '50px', height: '50px' }} aria-label="Instagram">
                <InstagramIcon size={22} color="var(--accent-gold)" />
              </a>
              <div>
                <h4 style={{ color: 'var(--text-main)', fontSize: '1.05rem' }}>Instagram</h4>
                <p style={{ fontSize: '0.9rem' }}>
                  <a href="https://instagram.com/smilestudios_official" target="_blank" rel="noreferrer" style={{ color: 'inherit' }}>@smilestudios_official</a>
                </p>
              </div>
            </div>

            <div style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
              <a href="https://wa.me/917200039833" target="_blank" rel="noreferrer" className="social-icon-btn" style={{ width: '50px', height: '50px' }} aria-label="WhatsApp">
                <MessageCircle size={22} color="var(--accent-gold)" />
              </a>
              <div>
                <h4 style={{ color: 'var(--text-main)', fontSize: '1.05rem' }}>WhatsApp</h4>
                <p style={{ fontSize: '0.9rem' }}>
                  <a href="https://wa.me/917200039833" target="_blank" rel="noreferrer" style={{ color: 'inherit' }}>Smile Studios</a>
                </p>
              </div>
            </div>

            <div style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
              <a href="https://facebook.com/Smilestudio_official" target="_blank" rel="noreferrer" className="social-icon-btn" style={{ width: '50px', height: '50px' }} aria-label="Facebook">
                <FacebookIcon size={22} color="var(--accent-gold)" />
              </a>
              <div>
                <h4 style={{ color: 'var(--text-main)', fontSize: '1.05rem' }}>Facebook</h4>
                <p style={{ fontSize: '0.9rem' }}>
                  <a href="https://facebook.com/Smilestudio_official" target="_blank" rel="noreferrer" style={{ color: 'inherit' }}>Smilestudio_official</a>
                </p>
              </div>
            </div>

            <div style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
              <a href="https://youtube.com/@smilestudio222" target="_blank" rel="noreferrer" className="social-icon-btn" style={{ width: '50px', height: '50px' }} aria-label="YouTube">
                <YoutubeIcon size={22} color="var(--accent-gold)" />
              </a>
              <div>
                <h4 style={{ color: 'var(--text-main)', fontSize: '1.05rem' }}>YouTube</h4>
                <p style={{ fontSize: '0.9rem' }}>
                  <a href="https://youtube.com/@smilestudio222" target="_blank" rel="noreferrer" style={{ color: 'inherit' }}>smilestudio222</a>
                </p>
              </div>
            </div>

            <div style={{ display: 'flex', gap: '1rem', alignItems: 'flex-start' }}>
              <div className="social-icon-btn" style={{ width: '50px', height: '50px', flexShrink: 0 }}>
                <MapPin size={22} color="var(--accent-gold)" />
              </div>
              <div>
                <h4 style={{ color: 'var(--text-main)', fontSize: '1.05rem' }}>Address</h4>
                <p style={{ fontSize: '0.9rem', lineHeight: 1.6 }}>224, Golden Street, 7th Cross St,<br />Thirumullaivoyal, Chennai,<br />Tamil Nadu 600062</p>
              </div>
            </div>
          </div>
        </div>
      </section>
    </>
  );
}

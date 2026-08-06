import React from 'react';
import { Link } from 'react-router-dom';
import { Phone, Mail, MapPin } from 'lucide-react';
import './Footer.css';

const InstagramIcon = ({ size = 18 }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <rect x="2" y="2" width="20" height="20" rx="5" ry="5"></rect>
    <path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z"></path>
    <line x1="17.5" y1="6.5" x2="17.51" y2="6.5"></line>
  </svg>
);

export function Footer() {
  return (
    <footer className="footer">
      <div className="container">
        <div className="footer-grid">
          {/* Brand Info */}
          <div className="footer-brand">
            <span className="brand-title">SMILE STUDIOS</span>
            <span className="brand-subtitle">PHOTOGRAPHY & FILMS</span>
            <p style={{ maxWidth: '320px', fontSize: '0.9rem' }}>
              Crafting fine art imagery, wedding stories, portraits, commercial events, and cinematography across India and globally.
            </p>
            <div className="social-links">
              <a href="https://instagram.com" target="_blank" rel="noreferrer" className="social-icon-btn" aria-label="Instagram">
                <InstagramIcon size={18} />
              </a>
              <a href="https://wa.me/919876543210" target="_blank" rel="noreferrer" className="social-icon-btn" aria-label="WhatsApp">
                <Phone size={18} />
              </a>
              <a href="mailto:hello@smilestudios.com" className="social-icon-btn" aria-label="Email">
                <Mail size={18} />
              </a>
            </div>
          </div>

          {/* Quick Links */}
          <div>
            <h4 className="footer-column-title">Explore</h4>
            <ul className="footer-links">
              <li><Link to="/">Home</Link></li>
              <li><Link to="/about">About Studio</Link></li>
              <li><Link to="/portfolio">All Galleries</Link></li>
              <li><Link to="/video">Cinematography & Films</Link></li>
              <li><Link to="/contact">Book Consultation</Link></li>
            </ul>
          </div>

          {/* Featured Categories */}
          <div>
            <h4 className="footer-column-title">Portfolio</h4>
            <ul className="footer-links">
              <li><Link to="/portfolio/portraits">Portraits</Link></li>
              <li><Link to="/portfolio/events">Events</Link></li>
              <li><Link to="/portfolio/maternity-baby">Maternity & Baby</Link></li>
              <li><Link to="/portfolio/tamil-weddings">Tamil Weddings</Link></li>
              <li><Link to="/portfolio/pre-weddings">Pre Weddings</Link></li>
            </ul>
          </div>

          {/* Contact Details */}
          <div>
            <h4 className="footer-column-title">Connect</h4>
            <ul className="footer-links">
              <li style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                <MapPin size={16} color="var(--accent-gold)" /> Chennai • Hyderabad • Bengaluru
              </li>
              <li style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                <Phone size={16} color="var(--accent-gold)" /> +91 98765 43210
              </li>
              <li style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                <Mail size={16} color="var(--accent-gold)" /> hello@smilestudios.com
              </li>
            </ul>
          </div>
        </div>

        {/* Bottom Bar */}
        <div className="footer-bottom">
          <p>© {new Date().getFullYear()} Smile Studios. All rights reserved.</p>
          <p style={{ display: 'flex', gap: '1.5rem' }}>
            <Link to="/admin" style={{ color: 'var(--text-dim)' }}>Admin Dashboard</Link>
          </p>
        </div>
      </div>
    </footer>
  );
}

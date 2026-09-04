import React from 'react';
import { Link } from 'react-router-dom';
import { Phone, MapPin, MessageCircle } from 'lucide-react';
import './Footer.css';

const InstagramIcon = ({ size = 18, color = 'currentColor' }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke={color} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <rect x="2" y="2" width="20" height="20" rx="5" ry="5"></rect>
    <path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z"></path>
    <line x1="17.5" y1="6.5" x2="17.51" y2="6.5"></line>
  </svg>
);

const FacebookIcon = ({ size = 18, color = 'currentColor' }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke={color} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <path d="M18 2h-3a5 5 0 0 0-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 0 1 1-1h3z"></path>
  </svg>
);

const YoutubeIcon = ({ size = 18, color = 'currentColor' }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke={color} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <path d="M2.5 17a24.12 24.12 0 0 1 0-10 2 2 0 0 1 1.4-1.4 49.56 49.56 0 0 1 16.2 0A2 2 0 0 1 21.5 7a24.12 24.12 0 0 1 0 10 2 2 0 0 1-1.4 1.4 49.55 49.55 0 0 1-16.2 0A2 2 0 0 1 2.5 17"></path>
    <path d="m10 15 5-3-5-3z"></path>
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
              <a
                href="https://instagram.com/smilestudios_official"
                target="_blank"
                rel="noreferrer"
                className="social-icon-btn"
                aria-label="Instagram"
              >
                <InstagramIcon size={18} />
              </a>
              <a
                href="https://wa.me/917200039833"
                target="_blank"
                rel="noreferrer"
                className="social-icon-btn"
                aria-label="WhatsApp"
              >
                <MessageCircle size={18} />
              </a>
              <a
                href="https://facebook.com/Smilestudio_official"
                target="_blank"
                rel="noreferrer"
                className="social-icon-btn"
                aria-label="Facebook"
              >
                <FacebookIcon size={18} />
              </a>
              <a
                href="https://youtube.com/@smilestudio222"
                target="_blank"
                rel="noreferrer"
                className="social-icon-btn"
                aria-label="YouTube"
              >
                <YoutubeIcon size={18} />
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
              <li><Link to="/contact">Book Consultation</Link></li>
            </ul>
          </div>

          {/* Featured Categories */}
          <div>
            <h4 className="footer-column-title">Portfolio</h4>
            <ul className="footer-links">
              <li><Link to="/portfolio/portraits">Portraits</Link></li>
              <li><Link to="/portfolio/pre-weddings">Pre Weddings</Link></li>
              <li><Link to="/portfolio/events">Events</Link></li>
              <li><Link to="/portfolio/maternity-baby">Maternity & Baby</Link></li>
              <li><Link to="/portfolio/weddings">Weddings</Link></li>
              <li><Link to="/portfolio/engagement">Engagement</Link></li>
            </ul>
          </div>

          {/* Contact Details */}
          <div>
            <h4 className="footer-column-title">Connect</h4>
            <ul className="footer-links footer-contact-list">
              <li className="footer-contact-item">
                <MapPin size={16} color="var(--accent-gold)" className="footer-contact-icon" />
                <span>224, Golden Street, 7th Cross St,<br />Thirumullaivoyal, Chennai,<br />Tamil Nadu 600062</span>
              </li>
              <li className="footer-contact-item">
                <Phone size={16} color="var(--accent-gold)" className="footer-contact-icon" />
                <a href="tel:+917200039833">+917200039833</a>
              </li>
              <li className="footer-contact-item">
                <MessageCircle size={16} color="var(--accent-gold)" className="footer-contact-icon" />
                <a href="https://wa.me/917200039833" target="_blank" rel="noreferrer">WhatsApp: Smile Studios</a>
              </li>
              <li className="footer-contact-item">
                <span className="footer-contact-icon"><InstagramIcon size={16} color="var(--accent-gold)" /></span>
                <a href="https://instagram.com/smilestudios_official" target="_blank" rel="noreferrer">@smilestudios_official</a>
              </li>
              <li className="footer-contact-item">
                <span className="footer-contact-icon"><FacebookIcon size={16} color="var(--accent-gold)" /></span>
                <a href="https://facebook.com/Smilestudio_official" target="_blank" rel="noreferrer">Smilestudio_official</a>
              </li>
              <li className="footer-contact-item">
                <span className="footer-contact-icon"><YoutubeIcon size={16} color="var(--accent-gold)" /></span>
                <a href="https://youtube.com/@smilestudio222" target="_blank" rel="noreferrer">smilestudio222</a>
              </li>
            </ul>
          </div>
        </div>

        {/* Bottom Bar */}
        <div className="footer-bottom">
          <p>© {new Date().getFullYear()} Smile Studios. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
}

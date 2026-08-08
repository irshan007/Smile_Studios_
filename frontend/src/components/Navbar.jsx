import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { ChevronDown, Menu, X } from 'lucide-react';
import './Navbar.css';

const CATEGORIES = [
  { name: 'Portraits', slug: 'portraits' },
  { name: 'Pre Weddings', slug: 'pre-weddings' },
  { name: 'Tamil Weddings', slug: 'tamil-weddings' },
  { name: 'Telugu Weddings', slug: 'telugu-weddings' },
  { name: 'Brahmin Weddings', slug: 'brahmin-weddings' },
  { name: 'Christian Weddings', slug: 'christian-weddings' },
  { name: 'Muslim Weddings', slug: 'muslim-weddings' },
  { name: 'Engagement', slug: 'engagement' },
  { name: 'Events', slug: 'events' },
  { name: 'Maternity/Baby', slug: 'maternity-baby' },
];

export function Navbar() {
  const [isMobileOpen, setIsMobileOpen] = useState(false);
  const [scrolled, setScrolled] = useState(false);
  const location = useLocation();

  useEffect(() => {
    const handleScroll = () => {
      setScrolled(window.scrollY > 30);
    };
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  useEffect(() => {
    setIsMobileOpen(false);
  }, [location.pathname]);

  return (
    <nav className={`navbar ${scrolled ? 'scrolled' : ''}`}>
      <div className="container navbar-container">
        <Link to="/" className="brand-logo">
          <span className="brand-title">SMILE STUDIOS</span>
          <span className="brand-subtitle">PHOTOGRAPHY & FILMS</span>
        </Link>

        {/* Desktop Menu */}
        <ul className="nav-menu">
          <li className="nav-item">
            <Link to="/" className={`nav-link ${location.pathname === '/' ? 'active' : ''}`}>
              Home
            </Link>
          </li>
          <li className="nav-item">
            <Link to="/about" className={`nav-link ${location.pathname === '/about' ? 'active' : ''}`}>
              About
            </Link>
          </li>
          <li className="nav-item">
            <Link to="/portfolio" className={`nav-link ${location.pathname.startsWith('/portfolio') ? 'active' : ''}`}>
              Portfolio <ChevronDown size={14} />
            </Link>
            <div className="dropdown-menu">
              {CATEGORIES.map((cat) => (
                <Link key={cat.slug} to={`/portfolio/${cat.slug}`} className="dropdown-item">
                  {cat.name}
                </Link>
              ))}
            </div>
          </li>
        </ul>

        {/* Mobile Toggle Button */}
        <button
          className="mobile-toggle"
          onClick={() => setIsMobileOpen(!isMobileOpen)}
          aria-label="Toggle navigation"
        >
          {isMobileOpen ? <X size={28} /> : <Menu size={28} />}
        </button>

        {/* Mobile Drawer */}
        <div className={`mobile-drawer ${isMobileOpen ? 'open' : ''}`}>
          <Link to="/" className="mobile-link">Home</Link>
          <Link to="/about" className="mobile-link">About</Link>
          <Link to="/portfolio" className="mobile-link">All Portfolio Categories</Link>
          <div style={{ paddingLeft: '1rem', display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
            {CATEGORIES.map((cat) => (
              <Link key={cat.slug} to={`/portfolio/${cat.slug}`} style={{ color: 'var(--text-muted)', fontSize: '0.95rem' }}>
                • {cat.name}
              </Link>
            ))}
          </div>
          <Link to="/video" className="mobile-link">Films</Link>
        </div>
      </div>
    </nav>
  );
}

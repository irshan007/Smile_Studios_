import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { ChevronDown, ChevronUp, Menu, X } from 'lucide-react';
import './Navbar.css';

const CATEGORIES = [
  { name: 'Portraits', slug: 'portraits' },
  { name: 'Weddings', slug: 'weddings' },
  { name: 'Engagement', slug: 'engagement' },
  { name: 'Events', slug: 'events' },
  { name: 'Maternity & Baby', slug: 'maternity-baby' },
];

export function Navbar() {
  const [isMobileOpen, setIsMobileOpen] = useState(false);
  const [isPortfolioExpanded, setIsPortfolioExpanded] = useState(false);
  const [scrolled, setScrolled] = useState(false);
  const location = useLocation();

  useEffect(() => {
    const handleScroll = () => {
      setScrolled(window.scrollY > 30);
    };
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  // Close mobile drawer and collapse accordion on navigation
  useEffect(() => {
    setIsMobileOpen(false);
    setIsPortfolioExpanded(false);
  }, [location.pathname]);

  // Lock background body scroll when mobile drawer is open
  useEffect(() => {
    if (isMobileOpen) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = '';
    }
    return () => {
      document.body.style.overflow = '';
    };
  }, [isMobileOpen]);

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
      </div>

      {/* Mobile Drawer Overlay */}
      <div className={`mobile-drawer ${isMobileOpen ? 'open' : ''}`}>
        <div className="mobile-drawer-content">
          <Link to="/" className="mobile-link" onClick={() => setIsMobileOpen(false)}>
            Home
          </Link>
          
          <Link to="/about" className="mobile-link" onClick={() => setIsMobileOpen(false)}>
            About
          </Link>

          <div className="mobile-portfolio-wrapper">
            <button
              type="button"
              className="mobile-portfolio-toggle"
              onClick={() => setIsPortfolioExpanded(!isPortfolioExpanded)}
            >
              <span>Portfolio</span>
              {isPortfolioExpanded ? (
                <ChevronUp size={20} className="accordion-icon" />
              ) : (
                <ChevronDown size={20} className="accordion-icon" />
              )}
            </button>

            {isPortfolioExpanded && (
              <div className="mobile-portfolio-sublist">
                <Link
                  to="/portfolio"
                  className="mobile-sublink mobile-sublink-all"
                  onClick={() => setIsMobileOpen(false)}
                >
                  All Portfolio Categories
                </Link>
                {CATEGORIES.map((cat) => (
                  <Link
                    key={cat.slug}
                    to={`/portfolio/${cat.slug}`}
                    className="mobile-sublink"
                    onClick={() => setIsMobileOpen(false)}
                  >
                    {cat.name}
                  </Link>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
}

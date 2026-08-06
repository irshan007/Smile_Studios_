import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { ArrowUpRight } from 'lucide-react';
import { SeoHead } from '../components/SeoHead';
import { CloudinaryImage } from '../components/CloudinaryImage';
import { fetchApi } from '../utils/api';
import './PortfolioCategories.css';

const DEFAULT_CATEGORIES = [
  { name: 'Portraits', slug: 'portraits', coverImageUrl: 'https://images.unsplash.com/photo-1519741497674-611481863552?auto=format&fit=crop&q=80&w=1200', imageCount: 14 },
  { name: 'Pre Weddings', slug: 'pre-weddings', coverImageUrl: 'https://images.unsplash.com/photo-1520854221256-17451cc331bf?auto=format&fit=crop&q=80&w=1200', imageCount: 18 },
  { name: 'Tamil Weddings', slug: 'tamil-weddings', coverImageUrl: 'https://images.unsplash.com/photo-1610030469983-98e550d6193c?auto=format&fit=crop&q=80&w=1200', imageCount: 22 },
  { name: 'Telugu Weddings', slug: 'telugu-weddings', coverImageUrl: 'https://images.unsplash.com/photo-1545232979-fbfd42e2006f?auto=format&fit=crop&q=80&w=1200', imageCount: 16 },
  { name: 'Brahmin Weddings', slug: 'brahmin-weddings', coverImageUrl: 'https://images.unsplash.com/photo-1511285560929-80b456fea0bc?auto=format&fit=crop&q=80&w=1200', imageCount: 15 },
  { name: 'Christian Weddings', slug: 'christian-weddings', coverImageUrl: 'https://images.unsplash.com/photo-1519225421980-715cb0215aed?auto=format&fit=crop&q=80&w=1200', imageCount: 19 },
  { name: 'Muslim Weddings', slug: 'muslim-weddings', coverImageUrl: 'https://images.unsplash.com/photo-1532712938310-34cb3982ef74?auto=format&fit=crop&q=80&w=1200', imageCount: 17 },
  { name: 'Engagement', slug: 'engagement', coverImageUrl: 'https://images.unsplash.com/photo-1465495976277-4387d4b0b4c6?auto=format&fit=crop&q=80&w=1200', imageCount: 12 },
  { name: 'Events', slug: 'events', coverImageUrl: 'https://images.unsplash.com/photo-1511795409834-ef04bbd61622?auto=format&fit=crop&q=80&w=1200', imageCount: 10 },
  { name: 'Maternity/Baby', slug: 'maternity-baby', coverImageUrl: 'https://images.unsplash.com/photo-1555252333-9f8e92e65df9?auto=format&fit=crop&q=80&w=1200', imageCount: 11 },
];

export function PortfolioCategories() {
  const [categories, setCategories] = useState(DEFAULT_CATEGORIES);

  useEffect(() => {
    async function loadCategories() {
      try {
        const res = await fetchApi('/gallery/categories');
        if (res?.data?.length > 0) {
          setCategories(res.data);
        }
      } catch (err) {
        console.warn('Backend API unavailable, displaying default categories:', err);
      }
    }
    loadCategories();
  }, []);

  return (
    <>
      <SeoHead
        title="Portfolio Categories"
        description="Explore Smile Studios' portfolio categories: Portraits, Pre Weddings, Weddings, Events, Maternity/Baby, and Engagement."
      />

      <div className="page-header">
        <div className="container">
          <span className="subtitle">EXPLORE GALLERIES</span>
          <h1>Portfolio Categories</h1>
        </div>
      </div>

      <section className="section-padding container">
        <div className="category-grid">
          {categories.map((cat) => (
            <Link key={cat.slug} to={`/portfolio/${cat.slug}`} className="category-card">
              <CloudinaryImage src={cat.coverImageUrl} alt={cat.name} className="category-card-bg" />
              <div className="category-card-overlay">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <span className="category-badge">{cat.imageCount} Photographs</span>
                  <div className="category-arrow">
                    <ArrowUpRight size={20} />
                  </div>
                </div>
                <h2 className="category-title">{cat.name}</h2>
              </div>
            </Link>
          ))}
        </div>
      </section>
    </>
  );
}

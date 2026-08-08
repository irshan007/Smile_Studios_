import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { ArrowUpRight } from 'lucide-react';
import { SeoHead } from '../components/SeoHead';
import { CloudinaryImage } from '../components/CloudinaryImage';
import { fetchApi } from '../utils/api';
import { categoryCovers } from '../data/portfolioAssets';
import './PortfolioCategories.css';

const DEFAULT_CATEGORIES = categoryCovers;

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
        description="Browse Smile Studios' weddings, pre-weddings, portraits, engagement stories, maternity sessions and event captures."
      />

      <div className="page-header">
        <div className="container">
          <span className="subtitle">EXPLORE GALLERIES</span>
          <h1>Portfolio Categories</h1>
          <p style={{ marginTop: '0.75rem', maxWidth: '760px', margin: '0.75rem auto 0' }}>
            Each collection reflects the calm, elegant atmosphere we create for our clients, from intimate portraits to full wedding weekend storytelling.
          </p>
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
                <div>
                  <h2 className="category-title">{cat.name}</h2>
                  <p style={{ color: 'var(--text-main)', marginTop: '0.5rem', maxWidth: '320px' }}>{cat.summary}</p>
                </div>
              </div>
            </Link>
          ))}
        </div>
      </section>
    </>
  );
}

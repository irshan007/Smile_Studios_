import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import Lightbox from 'yet-another-react-lightbox';
import 'yet-another-react-lightbox/styles.css';

import { SeoHead } from '../components/SeoHead';
import { CloudinaryImage } from '../components/CloudinaryImage';
import { fetchApi } from '../utils/api';

const CATEGORY_TITLES = {
  portraits: 'Portraits',
  'pre-weddings': 'Pre Weddings',
  'tamil-weddings': 'Tamil Weddings',
  'telugu-weddings': 'Telugu Weddings',
  'brahmin-weddings': 'Brahmin Weddings',
  'christian-weddings': 'Christian Weddings',
  'muslim-weddings': 'Muslim Weddings',
  engagement: 'Engagement',
  events: 'Events',
  'maternity-baby': 'Maternity/Baby',
};

const MOCK_CATEGORY_IMAGES = {
  'portraits': [
    { id: 1, url: 'https://images.unsplash.com/photo-1519741497674-611481863552?auto=format&fit=crop&q=80&w=1600', altText: 'Bridal Portraiture' },
    { id: 2, url: 'https://images.unsplash.com/photo-1511285560929-80b456fea0bc?auto=format&fit=crop&q=80&w=1600', altText: 'Editorial Silhouette' },
    { id: 3, url: 'https://images.unsplash.com/photo-1583939003579-730e3918a45a?auto=format&fit=crop&q=80&w=1600', altText: 'Royal Bride Grace' },
    { id: 4, url: 'https://images.unsplash.com/photo-1606800052052-a08af7148866?auto=format&fit=crop&q=80&w=1600', altText: 'Groom Elegance' },
  ],
  'pre-weddings': [
    { id: 1, url: 'https://images.unsplash.com/photo-1520854221256-17451cc331bf?auto=format&fit=crop&q=80&w=1600', altText: 'Beach Sunset Pre Wedding' },
    { id: 2, url: 'https://images.unsplash.com/photo-1532712938310-34cb3982ef74?auto=format&fit=crop&q=80&w=1600', altText: 'Palace Courtyard Romance' },
    { id: 3, url: 'https://images.unsplash.com/photo-1465495976277-4387d4b0b4c6?auto=format&fit=crop&q=80&w=1600', altText: 'Highland Sunset Embrace' },
  ],
  'tamil-weddings': [
    { id: 1, url: 'https://images.unsplash.com/photo-1610030469983-98e550d6193c?auto=format&fit=crop&q=80&w=1600', altText: 'Traditional Muhurtham' },
    { id: 2, url: 'https://images.unsplash.com/photo-1544078751-58fee2d8a03b?auto=format&fit=crop&q=80&w=1600', altText: 'Kanjivaram Saree Grace' },
    { id: 3, url: 'https://images.unsplash.com/photo-1519225421980-715cb0215aed?auto=format&fit=crop&q=80&w=1600', altText: 'Saptapadi Garland Moment' },
  ]
};

export function CategoryGallery() {
  const { category } = useParams();
  const [images, setImages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [lightboxOpen, setLightboxOpen] = useState(false);
  const [lightboxIdx, setLightboxIdx] = useState(0);

  const formattedCategory = category
    ? (CATEGORY_TITLES[category] || category.split('-').map((word) => word.charAt(0).toUpperCase() + word.slice(1)).join(' '))
    : 'Gallery';

  useEffect(() => {
    async function loadCategoryImages() {
      setLoading(true);
      try {
        const res = await fetchApi(`/gallery/${category}`);
        if (res?.data?.length > 0) {
          setImages(res.data);
        } else {
          setImages(MOCK_CATEGORY_IMAGES[category] || MOCK_CATEGORY_IMAGES['portraits']);
        }
      } catch (err) {
        console.warn(`Error loading gallery for ${category}, falling back to sample images:`, err);
        setImages(MOCK_CATEGORY_IMAGES[category] || MOCK_CATEGORY_IMAGES['portraits']);
      } finally {
        setLoading(false);
      }
    }
    loadCategoryImages();
  }, [category]);

  return (
    <>
      <SeoHead
        title={`${formattedCategory} Gallery`}
        description={`Browse our curated luxury ${formattedCategory} photography gallery showcasing timeless wedding moments.`}
      />

      <div className="page-header">
        <div className="container">
          <span className="subtitle">GALLERY COLLECTION</span>
          <h1>{formattedCategory}</h1>
          <p style={{ marginTop: '0.75rem' }}>
            <Link to="/portfolio" style={{ color: 'var(--accent-gold)' }}>← All Categories</Link>
          </p>
        </div>
      </div>

      <section className="section-padding container">
        {loading ? (
          <div style={{ textAlign: 'center', padding: '4rem 0', color: 'var(--text-muted)' }}>
            Loading gallery photographs...
          </div>
        ) : (
          <div style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))',
            gap: '1.5rem',
          }}>
            {images.map((img, index) => (
              <div
                key={img.id || index}
                onClick={() => {
                  setLightboxIdx(index);
                  setLightboxOpen(true);
                }}
                style={{
                  height: '420px',
                  borderRadius: 'var(--radius-md)',
                  overflow: 'hidden',
                  cursor: 'pointer',
                  position: 'relative',
                  border: '1px solid var(--border-light)',
                }}
              >
                <CloudinaryImage
                  src={img.url}
                  alt={img.altText || formattedCategory}
                />
              </div>
            ))}
          </div>
        )}
      </section>

      {/* Lightbox Modal */}
      <Lightbox
        open={lightboxOpen}
        close={() => setLightboxOpen(false)}
        index={lightboxIdx}
        slides={images.map(img => ({ src: img.url, alt: img.altText }))}
      />
    </>
  );
}

import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import Lightbox from 'yet-another-react-lightbox';
import 'yet-another-react-lightbox/styles.css';

import { SeoHead } from '../components/SeoHead';
import { categoryFallbacks } from '../data/portfolioAssets';
import './CategoryGallery.css';

const CATEGORY_TITLES = {
  portraits: 'Portraits',
  'pre-weddings': 'Pre Weddings',
  engagement: 'Engagement',
  events: 'Events',
  'maternity-baby': 'Maternity/Baby',
  weddings: 'Weddings',
  'tamil-weddings': 'Weddings',
  'telugu-weddings': 'Weddings',
  'brahmin-weddings': 'Weddings',
  'christian-weddings': 'Weddings',
  'muslim-weddings': 'Weddings',
};

export function CategoryGallery() {
  const { category } = useParams();
  const [images, setImages] = useState([]);
  const [lightboxOpen, setLightboxOpen] = useState(false);
  const [lightboxIdx, setLightboxIdx] = useState(0);

  const formattedCategory = category
    ? (CATEGORY_TITLES[category] || category.split('-').map((word) => word.charAt(0).toUpperCase() + word.slice(1)).join(' '))
    : 'Gallery';

  useEffect(() => {
    const galleryImages = categoryFallbacks[category] || categoryFallbacks['portraits'];
    setImages(galleryImages);
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
        <div className="category-gallery" aria-label={`${formattedCategory} gallery`}>
          {images.map((img, index) => (
            <button
              key={`${category}-${index}`}
              type="button"
              className="gallery-tile"
              onClick={() => {
                setLightboxIdx(index);
                setLightboxOpen(true);
              }}
              aria-label={`Open ${formattedCategory} photo ${index + 1}`}
            >
              <img
                src={img}
                alt={`${formattedCategory} photo ${index + 1}`}
                className="gallery-image"
                loading="lazy"
              />
              <div className="gallery-tile-overlay">
                <span className="gallery-tile-number">
                  {String(index + 1).padStart(2, '0')}
                </span>
                <span className="gallery-tile-caption">View Photograph</span>
              </div>
            </button>
          ))}
        </div>
      </section>

      <Lightbox
        open={lightboxOpen}
        close={() => setLightboxOpen(false)}
        index={lightboxIdx}
        slides={images.map((src) => ({ src, alt: formattedCategory }))}
      />
    </>
  );
}

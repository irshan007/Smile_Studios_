import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import Lightbox from 'yet-another-react-lightbox';
import 'yet-another-react-lightbox/styles.css';

import { SeoHead } from '../components/SeoHead';
import { categoryFallbacks } from '../data/portfolioAssets';
import { fetchApi } from '../utils/api';
import './CategoryGallery.css';

const CATEGORY_TITLES = {
  weddings: 'Weddings',
  'pre-weddings': 'Pre Weddings',
  engagement: 'Engagement',
  portraits: 'Portraits',
  events: 'Events',
  'maternity-baby': 'Maternity & Baby',
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
    let isMounted = true;

    async function loadCategoryWorks() {
      setLoading(true);
      try {
        const res = await fetchApi(`/works?category=${encodeURIComponent(category)}`);
        if (isMounted && res?.data && Array.isArray(res.data) && res.data.length > 0) {
          // Extract cover and gallery images from published works returned by backend
          const extractedImages = [];
          res.data.forEach((work) => {
            if (work.coverImageUrl) {
              extractedImages.push({
                src: work.coverImageUrl,
                title: work.title,
                alt: work.title || formattedCategory,
                workSlug: work.slug,
              });
            }
            if (work.images && Array.isArray(work.images)) {
              work.images.forEach((img) => {
                if (img.imageUrl && img.imageUrl !== work.coverImageUrl) {
                  extractedImages.push({
                    src: img.imageUrl,
                    title: img.altText || work.title || formattedCategory,
                    alt: img.altText || work.title || formattedCategory,
                    workSlug: work.slug,
                  });
                }
              });
            }
          });

          if (extractedImages.length > 0) {
            setImages(extractedImages);
            setLoading(false);
            return;
          }
        }
      } catch (err) {
        console.warn(`Backend works API call for '${category}' failed, falling back to static portfolio assets:`, err);
      }

      // Fallback to static portfolio assets
      if (isMounted) {
        const fallback = categoryFallbacks[category] || categoryFallbacks['portraits'];
        setImages(fallback || []);
        setLoading(false);
      }
    }

    loadCategoryWorks();

    return () => {
      isMounted = false;
    };
  }, [category, formattedCategory]);

  const getImageSrc = (item) => (typeof item === 'string' ? item : item.src);
  const getImageAlt = (item, idx) =>
    typeof item === 'string' ? `${formattedCategory} photo ${idx + 1}` : item.alt || item.title || `${formattedCategory} photo ${idx + 1}`;
  const getImageCaption = (item) => (typeof item === 'string' ? 'View Photograph' : item.title || 'View Photograph');

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
          <p style={{ textAlign: 'center', padding: '4rem 0', color: 'var(--text-muted)' }}>
            Loading gallery collection...
          </p>
        ) : images.length === 0 ? (
          <p style={{ textAlign: 'center', padding: '4rem 0', color: 'var(--text-muted)' }}>
            No published photographs found in this collection.
          </p>
        ) : (
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
                  src={getImageSrc(img)}
                  alt={getImageAlt(img, index)}
                  className="gallery-image"
                  loading="lazy"
                />
                <div className="gallery-tile-overlay">
                  <span className="gallery-tile-number">
                    {String(index + 1).padStart(2, '0')}
                  </span>
                  <span className="gallery-tile-caption">{getImageCaption(img)}</span>
                </div>
              </button>
            ))}
          </div>
        )}
      </section>

      <Lightbox
        open={lightboxOpen}
        close={() => setLightboxOpen(false)}
        index={lightboxIdx}
        slides={images.map((img, idx) => ({
          src: getImageSrc(img),
          alt: getImageAlt(img, idx),
        }))}
      />
    </>
  );
}

import React from 'react';
import { Helmet } from 'react-helmet-async';
import { seoImage } from '../data/portfolioAssets';

export function SeoHead({
  title = 'Smile Studios | Luxury Photography & Cinematography',
  description = 'Smile Studios is a refined photography studio creating timeless wedding, portrait and family stories with warmth, elegance and intention.',
  image = seoImage,
  url = window.location.href,
}) {
  const siteName = 'Smile Studios Photography & Films';
  const fullTitle = title.includes('Smile Studios') ? title : `${title} | ${siteName}`;

  return (
    <Helmet>
      {/* Standard Meta Tags */}
      <title>{fullTitle}</title>
      <meta name="description" content={description} />
      <link rel="canonical" href={url} />

      {/* OpenGraph / Facebook / WhatsApp */}
      <meta property="og:type" content="website" />
      <meta property="og:url" content={url} />
      <meta property="og:title" content={fullTitle} />
      <meta property="og:description" content={description} />
      <meta property="og:image" content={image} />
      <meta property="og:site_name" content={siteName} />

      {/* Twitter Card */}
      <meta name="twitter:card" content="summary_large_image" />
      <meta name="twitter:title" content={fullTitle} />
      <meta name="twitter:description" content={description} />
      <meta name="twitter:image" content={image} />
    </Helmet>
  );
}

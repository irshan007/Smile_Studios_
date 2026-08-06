import React from 'react';
import { Helmet } from 'react-helmet-async';

export function SeoHead({
  title = 'Smile Studios | Luxury Photography & Cinematography',
  description = 'Smile Studios is a premier full-service photography studio specializing in weddings, portraits, events, maternity/baby shoots, and cinematography.',
  image = 'https://images.unsplash.com/photo-1519741497674-611481863552?auto=format&fit=crop&q=80&w=1200',
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

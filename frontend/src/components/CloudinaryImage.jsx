import React, { useState } from 'react';
import { getResponsiveCloudinaryUrl } from '../utils/cloudinary';

export function CloudinaryImage({ src, alt, width = 1200, className = '', onClick, style }) {
  const [loaded, setLoaded] = useState(false);
  const optimizedUrl = getResponsiveCloudinaryUrl(src, width);

  return (
    <div
      className={`cloudinary-img-wrapper ${className}`}
      style={{ position: 'relative', overflow: 'hidden', ...style }}
    >
      <img
        src={optimizedUrl}
        alt={alt || 'Photography detail'}
        loading="lazy"
        onLoad={() => setLoaded(true)}
        onClick={onClick}
        style={{
          width: '100%',
          height: '100%',
          objectFit: 'cover',
          opacity: loaded ? 1 : 0,
          transition: 'opacity 0.6s cubic-bezier(0.16, 1, 0.3, 1), transform 0.6s cubic-bezier(0.16, 1, 0.3, 1)',
          cursor: onClick ? 'pointer' : 'default',
        }}
      />
      {!loaded && (
        <div
          style={{
            position: 'absolute',
            inset: 0,
            backgroundColor: '#1c1c21',
            animation: 'pulse 1.8s infinite ease-in-out',
          }}
        />
      )}
    </div>
  );
}

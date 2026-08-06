import React, { useState } from 'react';
import { Play } from 'lucide-react';

export function VideoFacade({ title, posterUrl, videoUrl }) {
  const [isPlaying, setIsPlaying] = useState(false);

  // Extract embed code or format URL for YouTube/Vimeo
  const getEmbedUrl = (url) => {
    if (url.includes('youtube.com/watch?v=')) {
      const id = url.split('v=')[1]?.split('&')[0];
      return `https://www.youtube.com/embed/${id}?autoplay=1&rel=0`;
    }
    if (url.includes('youtu.be/')) {
      const id = url.split('youtu.be/')[1]?.split('?')[0];
      return `https://www.youtube.com/embed/${id}?autoplay=1&rel=0`;
    }
    if (url.includes('vimeo.com/')) {
      const id = url.split('vimeo.com/')[1];
      return `https://player.vimeo.com/video/${id}?autoplay=1`;
    }
    return url;
  };

  return (
    <div style={{
      position: 'relative',
      width: '100%',
      paddingTop: '56.25%', // 16:9 Aspect Ratio
      backgroundColor: '#141417',
      borderRadius: '8px',
      overflow: 'hidden',
      border: '1px solid var(--border-light)',
    }}>
      {isPlaying ? (
        <iframe
          src={getEmbedUrl(videoUrl)}
          title={title}
          allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
          allowFullScreen
          style={{
            position: 'absolute',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
            border: 'none',
          }}
        />
      ) : (
        <div
          onClick={() => setIsPlaying(true)}
          style={{
            position: 'absolute',
            inset: 0,
            cursor: 'pointer',
            backgroundImage: `linear-gradient(to bottom, rgba(10, 10, 12, 0.2), rgba(10, 10, 12, 0.8)), url(${posterUrl})`,
            backgroundSize: 'cover',
            backgroundPosition: 'center',
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center',
            alignItems: 'center',
            gap: '1rem',
            transition: 'all 0.4s ease',
          }}
          className="video-facade-overlay"
        >
          <div style={{
            width: '70px',
            height: '70px',
            borderRadius: '50%',
            backgroundColor: 'rgba(212, 175, 55, 0.9)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            boxShadow: '0 10px 30px rgba(0, 0, 0, 0.5)',
            transform: 'scale(1)',
            transition: 'transform 0.3s ease, background-color 0.3s ease',
          }}>
            <Play size={28} color="#0a0a0c" fill="#0a0a0c" style={{ marginLeft: '4px' }} />
          </div>
          <h3 style={{
            fontSize: '1.25rem',
            fontFamily: 'var(--font-serif)',
            color: 'var(--text-main)',
            textAlign: 'center',
            padding: '0 1rem',
            textShadow: '0 2px 10px rgba(0,0,0,0.8)',
          }}>
            {title}
          </h3>
        </div>
      )}
    </div>
  );
}

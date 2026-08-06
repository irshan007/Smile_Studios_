import React from 'react';
import { SeoHead } from '../components/SeoHead';
import { VideoFacade } from '../components/VideoFacade';

const SAMPLE_FILMS = [
  {
    id: 1,
    title: 'The Royal Mahabalipuram Celebration • Ananya & Siddharth',
    posterUrl: 'https://images.unsplash.com/photo-1519741497674-611481863552?auto=format&fit=crop&q=80&w=1200',
    videoUrl: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
  },
  {
    id: 2,
    title: 'Heritage Palace Teaser • Priya & Vikram',
    posterUrl: 'https://images.unsplash.com/photo-1511285560929-80b456fea0bc?auto=format&fit=crop&q=80&w=1200',
    videoUrl: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
  },
  {
    id: 3,
    title: 'Sunset Coastal Vows • Sarah & David',
    posterUrl: 'https://images.unsplash.com/photo-1520854221256-17451cc331bf?auto=format&fit=crop&q=80&w=1200',
    videoUrl: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
  },
  {
    id: 4,
    title: 'Grand Gala Event Highlight • Smile Studios Films',
    posterUrl: 'https://images.unsplash.com/photo-1511795409834-ef04bbd61622?auto=format&fit=crop&q=80&w=1200',
    videoUrl: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
  },
];

export function VideoFilms() {
  return (
    <>
      <SeoHead
        title="Cinematography & Films"
        description="Cinematic films, wedding trailers, and commercial event films captured by Smile Studios."
      />

      <div className="page-header">
        <div className="container">
          <span className="subtitle">CINEMATIC STORIES & FILMS</span>
          <h1>Cinematography & Films</h1>
        </div>
      </div>

      <section className="section-padding container">
        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fill, minmax(440px, 1fr))',
          gap: '2.5rem',
        }}>
          {SAMPLE_FILMS.map((film) => (
            <VideoFacade
              key={film.id}
              title={film.title}
              posterUrl={film.posterUrl}
              videoUrl={film.videoUrl}
            />
          ))}
        </div>
      </section>
    </>
  );
}

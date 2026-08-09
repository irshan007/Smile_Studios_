import wedding1 from '../assets/Project_Source/wedding/c1.png';
import wedding2 from '../assets/Project_Source/wedding/c2.png';
import wedding3 from '../assets/Project_Source/wedding/c3.png';
import wedding4 from '../assets/Project_Source/wedding/c4.png';
import wedding5 from '../assets/Project_Source/wedding/c5.png';

import preWedding1 from '../assets/Project_Source/pre wedding/c1.png';
import preWedding2 from '../assets/Project_Source/pre wedding/c2.png';
import preWedding3 from '../assets/Project_Source/pre wedding/c3.png';
import preWedding4 from '../assets/Project_Source/pre wedding/c4.png';
import preWedding5 from '../assets/Project_Source/pre wedding/c5 (1).png';

import engagement1 from '../assets/Project_Source/engagement photos/c1.png';
import engagement2 from '../assets/Project_Source/engagement photos/c2.png';
import engagement3 from '../assets/Project_Source/engagement photos/c4.png';
import engagement4 from '../assets/Project_Source/engagement photos/c5.png';
import engagement5 from '../assets/Project_Source/engagement photos/engagement photos.png';

import maternity1 from '../assets/Project_Source/maternity/c1.png';
import maternity2 from '../assets/Project_Source/maternity/c2.png';
import maternity3 from '../assets/Project_Source/maternity/c3.png';
import maternity4 from '../assets/Project_Source/maternity/c4.png';
import maternity5 from '../assets/Project_Source/maternity/c5 (1).png';

import baby1 from '../assets/Project_Source/baby shoot/c1 (1).png';
import baby2 from '../assets/Project_Source/baby shoot/c2.png';
import baby3 from '../assets/Project_Source/baby shoot/c3.png';

import portrait1 from '../assets/Project_Source/puberty/c1.png';
import portrait2 from '../assets/Project_Source/puberty/c2.png';
import portrait3 from '../assets/Project_Source/puberty/c3.png';
import portrait4 from '../assets/Project_Source/puberty/c4.png';
import portrait5 from '../assets/Project_Source/puberty/c5 (1).png';

import reception1 from '../assets/Project_Source/reception/c1 (1).png';
import reception2 from '../assets/Project_Source/reception/c2.png';
import reception3 from '../assets/Project_Source/reception/c3.png';
import reception4 from '../assets/Project_Source/reception/c4.png';
import reception5 from '../assets/Project_Source/reception/c5.png';

import album1 from '../assets/Project_Source/albums/1.png';
import album2 from '../assets/Project_Source/albums/2.png';
import album3 from '../assets/Project_Source/albums/3.png';
import album4 from '../assets/Project_Source/albums/4.png';
import album5 from '../assets/Project_Source/albums/5.png';
import album6 from '../assets/Project_Source/albums/6.png';
import album7 from '../assets/Project_Source/albums/7.png';
import album8 from '../assets/Project_Source/albums/8.png';
import album9 from '../assets/Project_Source/albums/9.png';
import album10 from '../assets/Project_Source/albums/10.png';
import album11 from '../assets/Project_Source/albums/11.png';
import album12 from '../assets/Project_Source/albums/12.png';
import album13 from '../assets/Project_Source/albums/13.png';
import album14 from '../assets/Project_Source/albums/14.png';
import album15 from '../assets/Project_Source/albums/15.png';

export const heroSlides = [
  {
    src: wedding2,
    title: 'Romance, light, and lasting emotion',
    tag: 'Wedding Stories • Fine Art Photography',
  },
  {
    src: engagement2,
    title: 'Quiet luxury in every glance',
    tag: 'Engagement Sessions • Editorial Portraits',
  },
  {
    src: maternity1,
    title: 'Milestones told with tenderness',
    tag: 'Maternity & Family Moments',
  },
  {
    src: reception3,
    title: 'The energy of celebration, framed beautifully',
    tag: 'Events • Reception Storytelling',
  },
];

export const homeSelectedWorks = [
  { id: 1, src: wedding3, category: 'Weddings', altText: 'A graceful wedding portrait with natural light' },
  { id: 2, src: engagement1, category: 'Engagements', altText: 'A warm engagement session in soft evening light' },
  { id: 3, src: preWedding2, category: 'Pre-weddings', altText: 'A cinematic pre-wedding moment by the shoreline' },
  { id: 4, src: reception1, category: 'Events', altText: 'A vibrant reception scene full of movement and joy' },
  { id: 5, src: maternity3, category: 'Maternity & Baby', altText: 'An intimate maternity portrait with timeless warmth' },
  { id: 6, src: portrait2, category: 'Portraits', altText: 'A refined editorial portrait with confident presence' },
];

export const aboutGalleryImages = [wedding4, engagement3, maternity4, portrait3];

export const categoryCovers = [
  {
    name: 'Weddings',
    slug: 'weddings',
    coverImageUrl: wedding1,
    imageCount: 14,
    summary: 'Ceremony, reception, and family moments captured with calm detail.',
  },
  {
    name: 'Pre-Weddings',
    slug: 'pre-weddings',
    coverImageUrl: preWedding1,
    imageCount: 12,
    summary: 'Soft storytelling and intimate chemistry before the big day.',
  },
  {
    name: 'Engagements',
    slug: 'engagement',
    coverImageUrl: engagement2,
    imageCount: 10,
    summary: 'Romantic portraits that feel effortless and deeply personal.',
  },
  {
    name: 'Portraits',
    slug: 'portraits',
    coverImageUrl: portrait1,
    imageCount: 9,
    summary: 'Editorial portraits shaped by character, light, and confidence.',
  },
  {
    name: 'Maternity & Baby',
    slug: 'maternity-baby',
    coverImageUrl: maternity2,
    imageCount: 11,
    summary: 'Gentle, timeless images for growing families and cherished milestones.',
  },
  {
    name: 'Events',
    slug: 'events',
    coverImageUrl: reception2,
    imageCount: 8,
    summary: 'Candid energy and polished detail for memorable celebrations.',
  },
];

export const categoryFallbacks = {
  weddings: [wedding1, wedding2, wedding3, wedding4, wedding5, reception1],
  'pre-weddings': [preWedding1, preWedding2, preWedding3, preWedding4, preWedding5],
  engagement: [engagement1, engagement2, engagement3, engagement4, engagement5],
  portraits: [portrait1, portrait2, portrait3, portrait4, portrait5],
  'maternity-baby': [maternity1, maternity2, maternity3, maternity4, maternity5, baby1, baby2, baby3],
  events: [reception1, reception2, reception3, reception4, reception5, wedding5],
};



export const seoImage = engagement5;
export const studioAlbumImages = [album1, album2, album3, album4, album5, album6, album7, album8, album9, album10, album11, album12, album13, album14, album15];

// src/utils/galleryUtils.js
/**
 * Retrieves image URLs for a given gallery category.
 * In Phase 1 it reads a local JSON manifest located in the public folder.
 * The manifest is an array of image file names, e.g. ["img1.jpg", "img2.jpg"].
 * Returned URLs are relative to the public folder so they work with Vite's dev server.
 *
 * @param {string} category - The gallery category slug (e.g. "wedding")
 * @returns {Promise<string[]>} Array of image URLs
 */
export async function getGalleryImages(category) {
  const manifestUrl = `/gallery/${category}.json`;
  try {
    const response = await fetch(manifestUrl);
    if (!response.ok) {
      console.error(`Failed to load manifest for ${category}: ${response.status}`);
      return [];
    }
    const fileNames = await response.json();
    // Build full URLs for each image file in the public folder
    return fileNames.map((name) => `/gallery/${category}/${name}`);
  } catch (err) {
    console.error('Error fetching gallery manifest:', err);
    return [];
  }
}

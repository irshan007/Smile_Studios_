/**
 * Utility to inject Cloudinary optimization transformations (f_auto, q_auto, w_width)
 * @param {string} url - Original image URL
 * @param {number} width - Requested width for breakpoint
 * @returns {string} Optimized image URL
 */
export function getResponsiveCloudinaryUrl(url, width = 1200) {
  if (!url) return '';

  // Check if URL is a Cloudinary URL
  if (url.includes('res.cloudinary.com')) {
    const uploadIndex = url.indexOf('/upload/');
    if (uploadIndex !== -1) {
      const prefix = url.substring(0, uploadIndex + 8);
      const suffix = url.substring(uploadIndex + 8);
      return `${prefix}f_auto,q_auto,w_${width}/${suffix}`;
    }
  }

  // If Unsplash URL, append dynamic unsplash width parameter
  if (url.includes('images.unsplash.com')) {
    const baseUrl = url.split('?')[0];
    return `${baseUrl}?auto=format&fit=crop&q=80&w=${width}`;
  }

  return url;
}

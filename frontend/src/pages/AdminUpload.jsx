import React, { useState, useEffect, useCallback } from 'react';
import { Upload, Key, CheckCircle, AlertCircle, Image as ImageIcon, Trash2, Save, Layers } from 'lucide-react';
import { SeoHead } from '../components/SeoHead';
import { fetchApi } from '../utils/api';

const CATEGORIES = [
  'Portraits',
  'Pre Weddings',
  'Weddings',
  'Engagement',
  'Events',
  'Maternity/Baby',
];

export function AdminUpload() {
  const [apiKey, setApiKey] = useState(localStorage.getItem('admin_api_key') || '');
  const [authenticated, setAuthenticated] = useState(Boolean(apiKey));
  const [activeTab, setActiveTab] = useState('upload'); // 'upload' | 'manage'

  // Form State for Upload
  const [selectedFile, setSelectedFile] = useState(null);
  const [previewUrl, setPreviewUrl] = useState(null);
  const [category, setCategory] = useState('Portraits');
  const [altText, setAltText] = useState('');
  const [displayOrder, setDisplayOrder] = useState(1);
  const [heroOrder, setHeroOrder] = useState(1);
  const [selectedWorksOrder, setSelectedWorksOrder] = useState(1);

  const [isFeatured, setIsFeatured] = useState(false);
  const [showInHero, setShowInHero] = useState(false);
  const [showInSelectedWorks, setShowInSelectedWorks] = useState(false);

  const [uploading, setUploading] = useState(false);
  const [statusMessage, setStatusMessage] = useState(null);

  // Manage State
  const [imagesList, setImagesList] = useState([]);
  const [loadingList, setLoadingList] = useState(false);
  const [savingId, setSavingId] = useState(null);

  const handleKeyLogin = (e) => {
    e.preventDefault();
    if (apiKey.trim()) {
      localStorage.setItem('admin_api_key', apiKey.trim());
      setAuthenticated(true);
    }
  };

  const loadAdminImages = useCallback(async () => {
    if (!apiKey) return;
    setLoadingList(true);
    try {
      const res = await fetchApi('/admin/images', {
        headers: { 'X-API-KEY': apiKey },
      });
      if (res?.data) {
        setImagesList(res.data);
      }
    } catch (err) {
      console.error('Failed to fetch admin image list:', err);
      setStatusMessage({ type: 'error', text: err.message || 'Failed to load image management list.' });
    } finally {
      setLoadingList(false);
    }
  }, [apiKey]);

  useEffect(() => {
    if (authenticated && activeTab === 'manage') {
      loadAdminImages();
    }
  }, [authenticated, activeTab, loadAdminImages]);

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setSelectedFile(file);
      setPreviewUrl(URL.createObjectURL(file));
    }
  };

  const handleUpload = async (e) => {
    e.preventDefault();
    if (!selectedFile) {
      setStatusMessage({ type: 'error', text: 'Please select an image file to upload.' });
      return;
    }

    setUploading(true);
    setStatusMessage(null);

    const formData = new FormData();
    formData.append('file', selectedFile);
    formData.append('category', category);
    formData.append('altText', altText);
    formData.append('displayOrder', displayOrder);
    formData.append('heroOrder', heroOrder);
    formData.append('selectedWorksOrder', selectedWorksOrder);
    formData.append('isFeatured', isFeatured);
    formData.append('showInHero', showInHero);
    formData.append('showInSelectedWorks', showInSelectedWorks);

    try {
      const res = await fetchApi('/admin/images', {
        method: 'POST',
        headers: { 'X-API-KEY': apiKey },
        body: formData,
      });

      setStatusMessage({
        type: 'success',
        text: `Image uploaded & assigned placement! (ID: ${res.data.id})`,
      });

      // Reset upload form
      setSelectedFile(null);
      setPreviewUrl(null);
      setAltText('');
    } catch (err) {
      setStatusMessage({
        type: 'error',
        text: err.message || 'Failed to upload image. Please check your API Key.',
      });
    } finally {
      setUploading(false);
    }
  };

  // Inline management row state update
  const handleRowChange = (id, field, value) => {
    setImagesList((prev) =>
      prev.map((img) => (img.id === id ? { ...img, [field]: value } : img))
    );
  };

  // Save updated placement flags & orders via PATCH
  const handleSaveRow = async (img) => {
    setSavingId(img.id);
    setStatusMessage(null);

    try {
      const updatePayload = {
        category: img.category,
        altText: img.altText,
        displayOrder: Number(img.displayOrder) || 0,
        heroOrder: Number(img.heroOrder) || 0,
        selectedWorksOrder: Number(img.selectedWorksOrder) || 0,
        isFeatured: Boolean(img.isFeatured),
        showInHero: Boolean(img.showInHero),
        showInSelectedWorks: Boolean(img.showInSelectedWorks),
      };

      await fetchApi(`/admin/images/${img.id}`, {
        method: 'PATCH',
        headers: { 'X-API-KEY': apiKey },
        body: JSON.stringify(updatePayload),
      });

      setStatusMessage({ type: 'success', text: `Image #${img.id} placement & orders updated successfully!` });
    } catch (err) {
      setStatusMessage({ type: 'error', text: err.message || `Failed to update image #${img.id}` });
    } finally {
      setSavingId(null);
    }
  };

  // Delete image via DELETE
  const handleDeleteRow = async (id) => {
    if (!window.confirm(`Are you sure you want to delete Image #${id}? This action cannot be undone.`)) {
      return;
    }

    setStatusMessage(null);
    try {
      await fetchApi(`/admin/images/${id}`, {
        method: 'DELETE',
        headers: { 'X-API-KEY': apiKey },
      });

      setImagesList((prev) => prev.filter((img) => img.id !== id));
      setStatusMessage({ type: 'success', text: `Image #${id} deleted successfully.` });
    } catch (err) {
      setStatusMessage({ type: 'error', text: err.message || `Failed to delete image #${id}` });
    }
  };

  if (!authenticated) {
    return (
      <div className="section-padding container" style={{ maxWidth: '500px', marginTop: '6rem' }}>
        <SeoHead title="Admin Security Gate" description="Smile Studios Admin Gate" />
        <div style={{ background: 'var(--bg-surface)', border: '1px solid var(--border-light)', padding: '3rem 2rem', borderRadius: 'var(--radius-md)', textAlign: 'center' }}>
          <Key size={48} color="var(--accent-gold)" style={{ marginBottom: '1rem' }} />
          <h2 style={{ fontSize: '1.8rem', marginBottom: '0.5rem' }}>Smile Studios Admin</h2>
          <p style={{ fontSize: '0.9rem', marginBottom: '2rem' }}>
            Enter your secret API Key to access photo uploads and placement controls.
          </p>

          <form onSubmit={handleKeyLogin} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            <input
              type="password"
              className="form-input"
              placeholder="Enter X-API-KEY"
              value={apiKey}
              onChange={(e) => setApiKey(e.target.value)}
              required
            />
            <button type="submit" className="btn-primary" style={{ justifyContent: 'center' }}>
              Authenticate
            </button>
          </form>
        </div>
      </div>
    );
  }

  return (
    <>
      <SeoHead title="Admin Dashboard | Smile Studios" description="Manage Cloudinary uploads, placements, and display orders." />

      <div className="page-header">
        <div className="container">
          <span className="subtitle">SMILE STUDIOS ADMIN</span>
          <h1>Image & Placement Control</h1>
          <p style={{ marginTop: '0.5rem' }}>
            <button
              onClick={() => {
                localStorage.removeItem('admin_api_key');
                setAuthenticated(false);
              }}
              style={{ color: 'var(--accent-gold)', fontSize: '0.85rem', textDecoration: 'underline' }}
            >
              Sign Out (Clear Key)
            </button>
          </p>

          {/* Navigation Tabs */}
          <div style={{ display: 'flex', justifyContent: 'center', gap: '1rem', marginTop: '2rem' }}>
            <button
              className={activeTab === 'upload' ? 'btn-primary' : 'btn-outline'}
              onClick={() => setActiveTab('upload')}
              style={{ padding: '0.6rem 1.5rem', fontSize: '0.85rem' }}
            >
              <Upload size={16} /> Upload New Image
            </button>
            <button
              className={activeTab === 'manage' ? 'btn-primary' : 'btn-outline'}
              onClick={() => setActiveTab('manage')}
              style={{ padding: '0.6rem 1.5rem', fontSize: '0.85rem' }}
            >
              <Layers size={16} /> Manage Placements & Orders ({imagesList.length})
            </button>
          </div>
        </div>
      </div>

      <section className="section-padding container">
        {statusMessage && (
          <div
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '0.75rem',
              padding: '1rem',
              borderRadius: '4px',
              marginBottom: '2rem',
              background: statusMessage.type === 'success' ? 'rgba(34, 197, 94, 0.1)' : 'rgba(239, 68, 68, 0.1)',
              border: `1px solid ${statusMessage.type === 'success' ? '#22c55e' : '#ef4444'}`,
              color: statusMessage.type === 'success' ? '#22c55e' : '#ef4444',
              fontSize: '0.9rem',
              maxWidth: activeTab === 'upload' ? '800px' : '100%',
              margin: '0 auto 2rem',
            }}
          >
            {statusMessage.type === 'success' ? <CheckCircle size={20} /> : <AlertCircle size={20} />}
            <span>{statusMessage.text}</span>
          </div>
        )}

        {/* TAB 1: UPLOAD FORM */}
        {activeTab === 'upload' && (
          <div style={{ background: 'var(--bg-surface)', border: '1px solid var(--border-light)', padding: '3rem', borderRadius: 'var(--radius-md)', maxWidth: '800px', margin: '0 auto' }}>
            <form onSubmit={handleUpload} style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
              {/* File Picker Zone */}
              <div
                style={{
                  border: '2px dashed var(--border-light)',
                  borderRadius: 'var(--radius-md)',
                  padding: '2.5rem 1.5rem',
                  textAlign: 'center',
                  background: 'var(--bg-dark)',
                  cursor: 'pointer',
                }}
                onClick={() => document.getElementById('admin-file-input').click()}
              >
                {previewUrl ? (
                  <div>
                    <img src={previewUrl} alt="Upload preview" style={{ maxHeight: '240px', borderRadius: '8px', marginBottom: '1rem', objectFit: 'contain' }} />
                    <p style={{ fontSize: '0.85rem', color: 'var(--accent-gold)' }}>Click to change selected image</p>
                  </div>
                ) : (
                  <div>
                    <ImageIcon size={48} color="var(--accent-gold)" style={{ marginBottom: '1rem' }} />
                    <h4 style={{ color: 'var(--text-main)', fontSize: '1.1rem', marginBottom: '0.5rem' }}>Choose an Image File</h4>
                    <p style={{ fontSize: '0.85rem' }}>PNG, JPG, WEBP up to 10MB</p>
                  </div>
                )}
                <input
                  type="file"
                  id="admin-file-input"
                  accept="image/*"
                  onChange={handleFileChange}
                  style={{ display: 'none' }}
                />
              </div>

              {/* Category & Alt Text */}
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.5rem' }}>
                <div className="form-group">
                  <label>Gallery Category *</label>
                  <select
                    className="form-input"
                    value={category}
                    onChange={(e) => setCategory(e.target.value)}
                  >
                    {CATEGORIES.map((cat) => (
                      <option key={cat} value={cat}>
                        {cat}
                      </option>
                    ))}
                  </select>
                </div>

                <div className="form-group">
                  <label>Alt Text / Title</label>
                  <input
                    type="text"
                    className="form-input"
                    placeholder="e.g. Fine Art Portrait"
                    value={altText}
                    onChange={(e) => setAltText(e.target.value)}
                  />
                </div>
              </div>

              {/* Placement Checkboxes */}
              <div style={{ background: 'var(--bg-dark)', padding: '1.5rem', borderRadius: 'var(--radius-sm)', border: '1px solid var(--border-light)' }}>
                <h4 style={{ fontSize: '0.95rem', color: 'var(--accent-gold)', marginBottom: '1rem', textTransform: 'uppercase', letterSpacing: '0.08em' }}>
                  Image Placement Controls
                </h4>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '0.85rem' }}>
                  <label style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', cursor: 'pointer', color: 'var(--text-main)' }}>
                    <input
                      type="checkbox"
                      checked={showInHero}
                      onChange={(e) => setShowInHero(e.target.checked)}
                      style={{ width: '18px', height: '18px', accentColor: 'var(--accent-gold)' }}
                    />
                    Show in Homepage Hero Slideshow
                  </label>

                  <label style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', cursor: 'pointer', color: 'var(--text-main)' }}>
                    <input
                      type="checkbox"
                      checked={showInSelectedWorks}
                      onChange={(e) => setShowInSelectedWorks(e.target.checked)}
                      style={{ width: '18px', height: '18px', accentColor: 'var(--accent-gold)' }}
                    />
                    Show in Homepage "Selected Works" Grid
                  </label>

                  <label style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', cursor: 'pointer', color: 'var(--text-main)' }}>
                    <input
                      type="checkbox"
                      checked={isFeatured}
                      onChange={(e) => setIsFeatured(e.target.checked)}
                      style={{ width: '18px', height: '18px', accentColor: 'var(--accent-gold)' }}
                    />
                    Featured within Category Gallery Page
                  </label>
                </div>
              </div>

              {/* Display Orders */}
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <label>Hero Order #</label>
                  <input
                    type="number"
                    className="form-input"
                    value={heroOrder}
                    onChange={(e) => setHeroOrder(Number(e.target.value))}
                    min="1"
                    disabled={!showInHero}
                  />
                </div>

                <div className="form-group">
                  <label>Works Order #</label>
                  <input
                    type="number"
                    className="form-input"
                    value={selectedWorksOrder}
                    onChange={(e) => setSelectedWorksOrder(Number(e.target.value))}
                    min="1"
                    disabled={!showInSelectedWorks}
                  />
                </div>

                <div className="form-group">
                  <label>Category Order #</label>
                  <input
                    type="number"
                    className="form-input"
                    value={displayOrder}
                    onChange={(e) => setDisplayOrder(Number(e.target.value))}
                    min="1"
                  />
                </div>
              </div>

              <button
                type="submit"
                className="btn-primary"
                disabled={uploading}
                style={{ justifyContent: 'center', marginTop: '1rem' }}
              >
                {uploading ? 'Uploading to Cloudinary & DB...' : 'Upload & Assign Image'} <Upload size={18} />
              </button>
            </form>
          </div>
        )}

        {/* TAB 2: MANAGE IMAGES TABLE */}
        {activeTab === 'manage' && (
          <div style={{ background: 'var(--bg-surface)', border: '1px solid var(--border-light)', padding: '2rem', borderRadius: 'var(--radius-md)', overflowX: 'auto' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
              <h3 style={{ fontSize: '1.4rem', color: 'var(--text-main)' }}>
                All Uploaded Photos & Placements ({imagesList.length})
              </h3>
              <button className="btn-outline" onClick={loadAdminImages} style={{ padding: '0.4rem 1rem', fontSize: '0.8rem' }}>
                Refresh Table
              </button>
            </div>

            {loadingList ? (
              <p style={{ textAlign: 'center', padding: '3rem 0', color: 'var(--text-muted)' }}>Loading image management table...</p>
            ) : imagesList.length === 0 ? (
              <p style={{ textAlign: 'center', padding: '3rem 0', color: 'var(--text-muted)' }}>No images stored in database yet.</p>
            ) : (
              <table style={{ width: '100%', borderCollapse: 'collapse', color: 'var(--text-main)', fontSize: '0.85rem' }}>
                <thead>
                  <tr style={{ borderBottom: '1px solid var(--border-light)', textAlign: 'left', color: 'var(--accent-gold)' }}>
                    <th style={{ padding: '1rem 0.5rem' }}>Image</th>
                    <th style={{ padding: '1rem 0.5rem' }}>Category & Alt</th>
                    <th style={{ padding: '1rem 0.5rem' }}>Placements</th>
                    <th style={{ padding: '1rem 0.5rem' }}>Orders (Hero / Works / Cat)</th>
                    <th style={{ padding: '1rem 0.5rem', textAlign: 'right' }}>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {imagesList.map((img) => (
                    <tr key={img.id} style={{ borderBottom: '1px solid var(--border-light)' }}>
                      {/* Thumbnail */}
                      <td style={{ padding: '1rem 0.5rem' }}>
                        <img
                          src={img.url}
                          alt={img.altText || 'Thumbnail'}
                          style={{ width: '70px', height: '70px', objectFit: 'cover', borderRadius: '6px', border: '1px solid var(--border-light)' }}
                        />
                      </td>

                      {/* Category & Alt */}
                      <td style={{ padding: '1rem 0.5rem', minWidth: '200px' }}>
                        <select
                          className="form-input"
                          value={img.category}
                          onChange={(e) => handleRowChange(img.id, 'category', e.target.value)}
                          style={{ padding: '0.4rem 0.6rem', fontSize: '0.8rem', marginBottom: '0.4rem', width: '100%' }}
                        >
                          {CATEGORIES.map((cat) => (
                            <option key={cat} value={cat}>
                              {cat}
                            </option>
                          ))}
                        </select>
                        <input
                          type="text"
                          className="form-input"
                          value={img.altText || ''}
                          placeholder="Alt Text"
                          onChange={(e) => handleRowChange(img.id, 'altText', e.target.value)}
                          style={{ padding: '0.4rem 0.6rem', fontSize: '0.8rem', width: '100%' }}
                        />
                      </td>

                      {/* Placements */}
                      <td style={{ padding: '1rem 0.5rem', minWidth: '180px' }}>
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '0.4rem' }}>
                          <label style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', cursor: 'pointer' }}>
                            <input
                              type="checkbox"
                              checked={Boolean(img.showInHero)}
                              onChange={(e) => handleRowChange(img.id, 'showInHero', e.target.checked)}
                              style={{ accentColor: 'var(--accent-gold)' }}
                            />
                            Hero Slideshow
                          </label>

                          <label style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', cursor: 'pointer' }}>
                            <input
                              type="checkbox"
                              checked={Boolean(img.showInSelectedWorks)}
                              onChange={(e) => handleRowChange(img.id, 'showInSelectedWorks', e.target.checked)}
                              style={{ accentColor: 'var(--accent-gold)' }}
                            />
                            Selected Works
                          </label>

                          <label style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', cursor: 'pointer' }}>
                            <input
                              type="checkbox"
                              checked={Boolean(img.isFeatured)}
                              onChange={(e) => handleRowChange(img.id, 'isFeatured', e.target.checked)}
                              style={{ accentColor: 'var(--accent-gold)' }}
                            />
                            Category Featured
                          </label>
                        </div>
                      </td>

                      {/* Orders */}
                      <td style={{ padding: '1rem 0.5rem' }}>
                        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: '0.4rem', width: '180px' }}>
                          <input
                            type="number"
                            title="Hero Order"
                            className="form-input"
                            value={img.heroOrder || 0}
                            onChange={(e) => handleRowChange(img.id, 'heroOrder', e.target.value)}
                            style={{ padding: '0.3rem', fontSize: '0.8rem', textAlign: 'center' }}
                          />
                          <input
                            type="number"
                            title="Selected Works Order"
                            className="form-input"
                            value={img.selectedWorksOrder || 0}
                            onChange={(e) => handleRowChange(img.id, 'selectedWorksOrder', e.target.value)}
                            style={{ padding: '0.3rem', fontSize: '0.8rem', textAlign: 'center' }}
                          />
                          <input
                            type="number"
                            title="Category Order"
                            className="form-input"
                            value={img.displayOrder || 0}
                            onChange={(e) => handleRowChange(img.id, 'displayOrder', e.target.value)}
                            style={{ padding: '0.3rem', fontSize: '0.8rem', textAlign: 'center' }}
                          />
                        </div>
                      </td>

                      {/* Actions */}
                      <td style={{ padding: '1rem 0.5rem', textAlign: 'right' }}>
                        <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '0.5rem' }}>
                          <button
                            className="btn-primary"
                            onClick={() => handleSaveRow(img)}
                            disabled={savingId === img.id}
                            style={{ padding: '0.4rem 0.8rem', fontSize: '0.75rem' }}
                            title="Save Changes"
                          >
                            <Save size={14} /> {savingId === img.id ? '...' : 'Save'}
                          </button>
                          <button
                            className="btn-outline"
                            onClick={() => handleDeleteRow(img.id)}
                            style={{ padding: '0.4rem 0.8rem', fontSize: '0.75rem', borderColor: '#ef4444', color: '#ef4444' }}
                            title="Delete Image"
                          >
                            <Trash2 size={14} />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        )}
      </section>
    </>
  );
}
